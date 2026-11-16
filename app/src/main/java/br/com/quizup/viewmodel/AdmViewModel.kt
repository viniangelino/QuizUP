package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.quizup.data.local.NIVEIS
import br.com.quizup.data.local.Pergunta
import br.com.quizup.data.repository.PerguntaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdmUiState(
    // ---------- Painel (lista) ----------
    val todas: List<Pergunta> = emptyList(),       // tudo que veio do banco
    val perguntas: List<Pergunta> = emptyList(),   // já filtradas e ordenadas para a tela
    val filtroNivel: String? = null,               // null = todos os níveis
    val busca: String = "",
    // ---------- Formulário (nova / editar) ----------
    val idEditando: Int? = null,                   // null = pergunta nova
    val enunciado: String = "",
    val opcoes: List<String> = listOf("", "", "", ""),
    val correta: Int = 0,
    val dificuldade: String = "FACIL",
    val salvo: Boolean = false,
    // ---------- Avisos ----------
    val erro: String? = null,
    val mensagem: String? = null
)

/** Usado pelo Painel de perguntas (lista) e pela tela de formulário (nova ou editar). */
class AdmViewModel(private val repository: PerguntaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdmUiState())
    val uiState: StateFlow<AdmUiState> = _uiState.asStateFlow()

    init {
        // Read via Flow: a lista se atualiza sozinha a cada inserir/atualizar/deletar
        viewModelScope.launch {
            repository.buscarTodas().collect { resultado ->
                if (resultado.erro != null) {
                    _uiState.update { it.copy(erro = resultado.erro) }
                } else {
                    val todas = resultado.dados ?: emptyList()
                    _uiState.update { it.copy(todas = todas, perguntas = filtrar(todas, it.filtroNivel, it.busca)) }
                }
            }
        }
    }

    // ---------- Filtros e ordenação ----------

    fun filtrarPorNivel(codigo: String?) = _uiState.update {
        it.copy(filtroNivel = codigo, perguntas = filtrar(it.todas, codigo, it.busca))
    }

    fun alterarBusca(texto: String) = _uiState.update {
        it.copy(busca = texto, perguntas = filtrar(it.todas, it.filtroNivel, texto))
    }

    /** Regra de negócio da lista: filter por nível, filter pelo texto e sortedBy pela ordem dos níveis. */
    private fun filtrar(todas: List<Pergunta>, nivel: String?, busca: String): List<Pergunta> {
        return todas
            .filter { nivel == null || it.dificuldade == nivel }
            .filter { it.enunciado.contains(busca.trim(), ignoreCase = true) }
            .sortedBy { pergunta -> NIVEIS.indexOfFirst { it.codigo == pergunta.dificuldade } }
    }

    // ---------- Delete ----------

    fun remover(pergunta: Pergunta) {
        viewModelScope.launch {
            val resultado = repository.deletar(pergunta)
            _uiState.update { it.copy(mensagem = resultado.erro ?: "Pergunta removida") }
        }
    }

    // ---------- Formulário ----------

    /** Editar: busca a pergunta no banco pelo id e preenche os campos. */
    fun carregarParaEditar(id: Int) {
        if (_uiState.value.idEditando == id) return   // já carregou
        viewModelScope.launch {
            val resultado = repository.buscarPorId(id)
            val p = resultado.dados
            if (p == null) {
                _uiState.update { it.copy(erro = resultado.erro) }
            } else {
                _uiState.update {
                    it.copy(
                        idEditando = p.id,
                        enunciado = p.enunciado,
                        opcoes = listOf(p.opcaoA, p.opcaoB, p.opcaoC, p.opcaoD),
                        correta = p.correta,
                        dificuldade = p.dificuldade
                    )
                }
            }
        }
    }

    fun alterarEnunciado(valor: String) = _uiState.update { it.copy(enunciado = valor, erro = null) }

    fun alterarOpcao(posicao: Int, valor: String) {
        val novas = _uiState.value.opcoes.toMutableList()
        novas[posicao] = valor
        _uiState.update { it.copy(opcoes = novas, erro = null) }
    }

    fun escolherCorreta(posicao: Int) = _uiState.update { it.copy(correta = posicao) }

    fun escolherDificuldade(codigo: String) = _uiState.update { it.copy(dificuldade = codigo) }

    /** Create (pergunta nova) ou Update (editando). */
    fun salvar() {
        val estado = _uiState.value
        if (estado.enunciado.isBlank()) {
            _uiState.update { it.copy(erro = "Escreva a pergunta") }
            return
        }
        if (estado.opcoes.any { it.isBlank() }) {
            _uiState.update { it.copy(erro = "Preencha as 4 alternativas") }
            return
        }

        val pergunta = Pergunta(
            id = estado.idEditando ?: 0,
            enunciado = estado.enunciado.trim(),
            opcaoA = estado.opcoes[0].trim(),
            opcaoB = estado.opcoes[1].trim(),
            opcaoC = estado.opcoes[2].trim(),
            opcaoD = estado.opcoes[3].trim(),
            correta = estado.correta,
            dificuldade = estado.dificuldade
        )

        viewModelScope.launch {
            val resultado = if (estado.idEditando == null) repository.inserir(pergunta)
            else repository.atualizar(pergunta)

            if (resultado.erro != null) {
                _uiState.update { it.copy(erro = resultado.erro) }
            } else {
                val texto = if (estado.idEditando == null) "Pergunta salva no banco!" else "Pergunta atualizada!"
                _uiState.update { it.copy(salvo = true, mensagem = texto) }
            }
        }
    }

    fun mensagemMostrada() = _uiState.update { it.copy(mensagem = null) }
}
