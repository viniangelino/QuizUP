package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.quizup.data.repository.TimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Um time pronto para a tela (já em português e sem campos vazios). */
data class TimeUi(
    val nome: String,
    val pais: String,
    val liga: String,
    val estadio: String,
    val fundacao: String,
    val ehDoBrasil: Boolean
)

data class TimesUiState(
    val busca: String = "",
    val todos: List<TimeUi> = emptyList(),       // o que veio da API
    val times: List<TimeUi> = emptyList(),       // o que aparece na tela (com o filtro)
    val soBrasil: Boolean = false,
    val carregando: Boolean = false,
    val jaBuscou: Boolean = false,
    val erro: String? = null
)

class TimesViewModel(private val repository: TimeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TimesUiState())
    val uiState: StateFlow<TimesUiState> = _uiState.asStateFlow()

    fun alterarBusca(texto: String) = _uiState.update { it.copy(busca = texto, erro = null) }

    fun buscar() {
        val nome = _uiState.value.busca.trim()
        if (nome.isEmpty()) return
        _uiState.update { it.copy(carregando = true, erro = null) }

        viewModelScope.launch {
            val resultado = repository.buscarTimes(nome)
            if (resultado.erro != null) {
                _uiState.update { it.copy(carregando = false, erro = resultado.erro) }
            } else {
                // map: transforma o que veio da API (TimeDto) no que a tela mostra (TimeUi)
                val todos = (resultado.dados ?: emptyList())
                    .map { dto ->
                        TimeUi(
                            nome = dto.strTeam ?: "Sem nome",
                            pais = dto.strCountry ?: "-",
                            liga = dto.strLeague ?: "-",
                            estadio = dto.strStadium ?: "-",
                            fundacao = dto.intFormedYear ?: "-",
                            ehDoBrasil = dto.strCountry == "Brazil"
                        )
                    }
                    .sortedBy { it.nome }
                _uiState.update {
                    it.copy(todos = todos, times = filtrar(todos, it.soBrasil), carregando = false, jaBuscou = true)
                }
            }
        }
    }

    /** Checkbox "Só times do Brasil". */
    fun alternarSoBrasil(marcado: Boolean) = _uiState.update {
        it.copy(soBrasil = marcado, times = filtrar(it.todos, marcado))
    }

    private fun filtrar(todos: List<TimeUi>, soBrasil: Boolean): List<TimeUi> {
        return if (soBrasil) todos.filter { it.ehDoBrasil } else todos
    }
}
