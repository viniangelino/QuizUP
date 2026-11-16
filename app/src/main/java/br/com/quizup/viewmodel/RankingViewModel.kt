package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.quizup.data.local.Pontuacao
import br.com.quizup.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Uma linha pronta para a tela do ranking. */
data class LinhaRanking(
    val posicao: Int,
    val nome: String,
    val pontos: Int,
    val acertos: Int,
    val total: Int,
    val souEu: Boolean
)

data class RankingUiState(
    val todas: List<Pontuacao> = emptyList(),     // tudo que veio do banco
    val nivel: String = "FACIL",                  // nível escolhido nos botões do topo
    val linhas: List<LinhaRanking> = emptyList(), // top 10 do nível, pronto para a tela
    val minhaPontuacao: Pontuacao? = null,        // resultado de quem acabou de jogar
    val carregando: Boolean = true,
    val erro: String? = null
)

/**
 * @param pontuacaoId id da partida que acabou de ser salva, ou -1 quando o ranking
 * foi aberto pela barra de navegação.
 */
class RankingViewModel(
    private val repository: RankingRepository,
    nivelInicial: String,
    private val pontuacaoId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState(nivel = nivelInicial))
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.buscarTodas().collect { resultado ->
                if (resultado.erro != null) {
                    _uiState.update { it.copy(erro = resultado.erro, carregando = false) }
                } else {
                    val todas = resultado.dados ?: emptyList()
                    _uiState.update {
                        it.copy(
                            todas = todas,
                            minhaPontuacao = todas.find { p -> p.id == pontuacaoId },
                            linhas = montarTop10(todas, it.nivel),
                            carregando = false
                        )
                    }
                }
            }
        }
    }

    /** Botões Fácil / Médio / Difícil / Expert no topo do ranking. */
    fun escolherNivel(codigo: String) = _uiState.update {
        it.copy(nivel = codigo, linhas = montarTop10(it.todas, codigo))
    }

    /** Regra de negócio: filter pelo nível, sortedByDescending pelos pontos, top 10 e map para a linha da tela. */
    private fun montarTop10(todas: List<Pontuacao>, nivel: String): List<LinhaRanking> {
        return todas
            .filter { it.dificuldade == nivel }
            .sortedByDescending { it.pontos }
            .take(10)
            .mapIndexed { i, p ->
                LinhaRanking(
                    posicao = i + 1,
                    nome = p.nome,
                    pontos = p.pontos,
                    acertos = p.acertos,
                    total = p.total,
                    souEu = p.id == pontuacaoId
                )
            }
    }
}
