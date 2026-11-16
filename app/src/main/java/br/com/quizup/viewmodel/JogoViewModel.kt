package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.quizup.data.local.Nivel
import br.com.quizup.data.local.PERGUNTAS_POR_JOGO
import br.com.quizup.data.local.Pergunta
import br.com.quizup.data.local.Pontuacao
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Tudo o que a tela da pergunta precisa para se desenhar. */
data class JogoUiState(
    val carregando: Boolean = true,
    val perguntas: List<Pergunta> = emptyList(),
    val indice: Int = 0,                // qual pergunta está na tela
    val respostaEscolhida: Int? = null, // null = ainda não respondeu
    val pontos: Int = 0,
    val acertos: Int = 0,
    val pontuacaoId: Int? = null,       // partida salva no banco: a tela abre o ranking
    val erro: String? = null
)

class JogoViewModel(
    private val perguntaRepository: PerguntaRepository,
    private val rankingRepository: RankingRepository,
    private val nivel: Nivel,
    private val nomeJogador: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(JogoUiState())
    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()

    init {
        // Busca as perguntas do nível no banco, embaralha e pega 10
        viewModelScope.launch {
            val resultado = perguntaRepository.buscarPorDificuldade(nivel.codigo)
            val sorteadas = (resultado.dados ?: emptyList()).shuffled().take(PERGUNTAS_POR_JOGO)
            _uiState.update { it.copy(perguntas = sorteadas, carregando = false, erro = resultado.erro) }
        }
    }

    /** Jogador tocou numa alternativa (0 = A ... 3 = D). */
    fun responder(opcao: Int) {
        val estado = _uiState.value
        if (estado.respostaEscolhida != null) return   // já respondeu esta pergunta

        val pergunta = estado.perguntas[estado.indice]
        if (opcao == pergunta.correta) {
            _uiState.update {
                it.copy(respostaEscolhida = opcao, pontos = it.pontos + nivel.pontos, acertos = it.acertos + 1)
            }
        } else {
            _uiState.update { it.copy(respostaEscolhida = opcao) }
        }
    }

    /** Botão "Próxima": vai para a próxima pergunta ou, se acabou, salva o resultado. */
    fun proxima() {
        val estado = _uiState.value
        if (estado.indice + 1 < estado.perguntas.size) {
            _uiState.update { it.copy(indice = it.indice + 1, respostaEscolhida = null) }
        } else {
            salvarResultado()
        }
    }

    private fun salvarResultado() {
        val estado = _uiState.value
        viewModelScope.launch {
            val resultado = rankingRepository.salvar(
                Pontuacao(
                    nome = nomeJogador,
                    pontos = estado.pontos,
                    acertos = estado.acertos,
                    total = estado.perguntas.size,
                    dificuldade = nivel.codigo
                )
            )
            _uiState.update { it.copy(pontuacaoId = resultado.dados, erro = resultado.erro) }
        }
    }

    fun erroMostrado() = _uiState.update { it.copy(erro = null) }
}
