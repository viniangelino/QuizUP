package br.com.quizup.data.repository

/**
 * O que o Repository devolve para o ViewModel:
 * - deu certo  -> Resultado(dados = ...)
 * - deu errado -> Resultado(erro = "mensagem já pronta para mostrar na tela")
 *
 * Assim o try-catch fica todo no Repository, e o ViewModel só copia o erro para o UiState.
 * O <T> quer dizer "qualquer tipo": Resultado<List<Pergunta>>, Resultado<Int>...
 */
data class Resultado<T>(
    val dados: T? = null,
    val erro: String? = null
)
