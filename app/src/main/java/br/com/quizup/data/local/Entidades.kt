package br.com.quizup.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Um nível de dificuldade do jogo. */
data class Nivel(
    val codigo: String,   // o que fica salvo no banco: "FACIL", "MEDIO"...
    val nome: String,     // o que aparece na tela: "Fácil", "Médio"...
    val pontos: Int,      // pontos por resposta certa
    val barras: Int       // quantos quadradinhos acesos no botão (1 a 4)
)

val NIVEIS = listOf(
    Nivel("FACIL", "Fácil", 10, 1),
    Nivel("MEDIO", "Médio", 20, 2),
    Nivel("DIFICIL", "Difícil", 30, 3),
    Nivel("EXPERT", "Expert", 50, 4)
)

/** Procura o nível pelo código (se não achar, devolve o Fácil). */
fun buscarNivel(codigo: String?): Nivel {
    return NIVEIS.find { it.codigo == codigo } ?: NIVEIS[0]
}

const val PERGUNTAS_POR_JOGO = 10

/** Tabela "perguntas". O ADM cadastra novas escolhendo a dificuldade. */
@Entity(tableName = "perguntas")
data class Pergunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val enunciado: String,
    val opcaoA: String,
    val opcaoB: String,
    val opcaoC: String,
    val opcaoD: String,
    val correta: Int,          // 0 = A, 1 = B, 2 = C, 3 = D
    val dificuldade: String    // código do nível: "FACIL", "MEDIO", "DIFICIL" ou "EXPERT"
)

/** As 4 alternativas da pergunta em uma lista (fica mais fácil de mostrar na tela). */
fun alternativasDe(pergunta: Pergunta): List<String> {
    return listOf(pergunta.opcaoA, pergunta.opcaoB, pergunta.opcaoC, pergunta.opcaoD)
}

/** Tabela "pontuacoes": o ranking. */
@Entity(tableName = "pontuacoes")
data class Pontuacao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val pontos: Int,
    val acertos: Int,
    val total: Int,
    val dificuldade: String
)
