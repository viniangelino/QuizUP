package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Controle de perfil: quem está usando o app é Jogador ou Administrador?
 * Um único SessaoViewModel é compartilhado pelo app todo (criado no AppNavigation).
 * A barra de navegação lê ehAdmin para mostrar ou esconder a aba "Perguntas".
 */
data class SessaoUiState(
    val ehAdmin: Boolean = false,
    val usuario: String = "",
    val senha: String = "",
    val erro: String? = null
)

class SessaoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessaoUiState())
    val uiState: StateFlow<SessaoUiState> = _uiState.asStateFlow()

    fun alterarUsuario(valor: String) = _uiState.update { it.copy(usuario = valor, erro = null) }

    fun alterarSenha(valor: String) = _uiState.update { it.copy(senha = valor, erro = null) }

    fun entrarComoAdmin() {
        val estado = _uiState.value
        if (estado.usuario.trim() == "admin" && estado.senha == "admin") {
            _uiState.update { it.copy(ehAdmin = true, senha = "", erro = null) }
        } else {
            _uiState.update { it.copy(erro = "Usuário ou senha inválidos") }
        }
    }

    /** Volta a ser Jogador: a aba Perguntas some. */
    fun sair() = _uiState.update { SessaoUiState() }
}
