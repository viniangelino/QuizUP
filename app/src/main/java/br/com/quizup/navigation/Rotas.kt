package br.com.quizup.navigation

import android.net.Uri

/** Todas as rotas do app em um só lugar. */
object Rotas {
    const val INICIO = "inicio"
    const val DIFICULDADE = "dificuldade"
    const val EXPLICACAO = "explicacao/{nivel}"
    const val JOGO = "jogo/{nivel}/{nome}"
    const val RANKING = "ranking/{nivel}/{pontuacaoId}"
    const val TIMES = "times"
    const val PERFIL = "perfil"
    const val ADM_PAINEL = "adm_painel"
    const val ADM_PERGUNTA = "adm_pergunta/{id}"

    // Montam a rota com os valores de verdade
    fun explicacao(nivel: String) = "explicacao/$nivel"

    // Uri.encode: deixa o nome seguro para ir na rota (espaços e acentos)
    fun jogo(nivel: String, nome: String) = "jogo/$nivel/${Uri.encode(nome)}"

    // pontuacaoId = -1 quando o ranking é aberto pela barra (sem ter acabado de jogar)
    fun ranking(nivel: String, pontuacaoId: Int) = "ranking/$nivel/$pontuacaoId"

    // id = -1 para pergunta nova; id da pergunta para editar
    fun admPergunta(id: Int) = "adm_pergunta/$id"
}
