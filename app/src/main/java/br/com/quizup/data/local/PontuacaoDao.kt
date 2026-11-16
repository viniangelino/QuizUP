package br.com.quizup.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PontuacaoDao {

    /** Devolve o id gerado (para destacar o jogador no ranking). */
    @Insert
    suspend fun inserir(pontuacao: Pontuacao): Long

    @Insert
    suspend fun inserirTodas(pontuacoes: List<Pontuacao>)

    /** Todas as pontuações. Filtrar por nível e ordenar fica no RankingViewModel. */
    @Query("SELECT * FROM pontuacoes")
    fun buscarTodas(): Flow<List<Pontuacao>>

    @Query("SELECT COUNT(*) FROM pontuacoes")
    suspend fun contar(): Int
}
