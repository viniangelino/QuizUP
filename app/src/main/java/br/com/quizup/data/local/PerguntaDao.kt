package br.com.quizup.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PerguntaDao {

    // ---------- C (Create) ----------
    @Insert
    suspend fun inserir(pergunta: Pergunta)

    /** Usado só para colocar as perguntas iniciais de uma vez. */
    @Insert
    suspend fun inserirTodas(perguntas: List<Pergunta>)

    // ---------- R (Read) ----------
    /** Painel do ADM: o Flow atualiza a lista sozinho quando uma pergunta entra, muda ou sai. */
    @Query("SELECT * FROM perguntas")
    fun buscarTodas(): Flow<List<Pergunta>>

    /** Partida: todas as perguntas do nível escolhido. */
    @Query("SELECT * FROM perguntas WHERE dificuldade = :dificuldade")
    suspend fun buscarPorDificuldade(dificuldade: String): List<Pergunta>

    /** Editar: busca a pergunta pelo id que veio na rota. */
    @Query("SELECT * FROM perguntas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Pergunta?

    @Query("SELECT COUNT(*) FROM perguntas")
    suspend fun contar(): Int

    // ---------- U (Update) ----------
    @Update
    suspend fun atualizar(pergunta: Pergunta)

    // ---------- D (Delete) ----------
    @Delete
    suspend fun deletar(pergunta: Pergunta)
}
