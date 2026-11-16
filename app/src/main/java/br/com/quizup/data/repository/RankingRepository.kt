package br.com.quizup.data.repository

import android.util.Log
import br.com.quizup.data.local.Pontuacao
import br.com.quizup.data.local.PontuacaoDao
import br.com.quizup.data.local.RANKING_FALSO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val TAG = "RankingRepository"

/** Única porta de entrada para o ranking. Só ele fala com o PontuacaoDao. */
class RankingRepository(private val dao: PontuacaoDao) {

    fun buscarTodas(): Flow<Resultado<List<Pontuacao>>> {
        return dao.buscarTodas()
            .map { lista -> Resultado(dados = lista) }
            .catch { e ->
                Log.e(TAG, "Erro ao ler o ranking", e)
                emit(Resultado(erro = "Não foi possível carregar o ranking"))
            }
    }

    /** Salva o resultado da partida e devolve o id gerado pelo banco. */
    suspend fun salvar(pontuacao: Pontuacao): Resultado<Int> {
        return try {
            Resultado(dados = dao.inserir(pontuacao).toInt())
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao salvar pontuação", e)
            Resultado(erro = "Não foi possível salvar sua pontuação")
        }
    }

    /** Se o ranking ainda está vazio (primeira abertura), coloca o ranking falso. */
    suspend fun popularSeVazio() {
        try {
            if (dao.contar() == 0) dao.inserirTodas(RANKING_FALSO)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao colocar o ranking inicial", e)
        }
    }
}
