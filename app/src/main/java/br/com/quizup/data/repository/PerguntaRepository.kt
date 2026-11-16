package br.com.quizup.data.repository

import android.util.Log
import br.com.quizup.data.local.PERGUNTAS_INICIAIS
import br.com.quizup.data.local.Pergunta
import br.com.quizup.data.local.PerguntaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val TAG = "PerguntaRepository"

/**
 * Única porta de entrada para as perguntas. Só ele fala com o PerguntaDao.
 * Todo acesso ao banco fica dentro de try-catch: se o banco falhar, devolve a mensagem de erro.
 */
class PerguntaRepository(private val dao: PerguntaDao) {

    /** Lista reativa: cada mudança na tabela chega como um novo Resultado. */
    fun buscarTodas(): Flow<Resultado<List<Pergunta>>> {
        return dao.buscarTodas()
            .map { lista -> Resultado(dados = lista) }
            .catch { e ->
                Log.e(TAG, "Erro ao ler perguntas", e)
                emit(Resultado(erro = "Não foi possível carregar as perguntas"))
            }
    }

    suspend fun buscarPorDificuldade(dificuldade: String): Resultado<List<Pergunta>> {
        return try {
            Resultado(dados = dao.buscarPorDificuldade(dificuldade))
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar perguntas do nível $dificuldade", e)
            Resultado(erro = "Não foi possível carregar as perguntas")
        }
    }

    suspend fun buscarPorId(id: Int): Resultado<Pergunta> {
        return try {
            val pergunta = dao.buscarPorId(id)
            if (pergunta == null) Resultado(erro = "Pergunta não encontrada") else Resultado(dados = pergunta)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar pergunta $id", e)
            Resultado(erro = "Não foi possível carregar a pergunta")
        }
    }

    suspend fun inserir(pergunta: Pergunta): Resultado<Unit> {
        return try {
            dao.inserir(pergunta)
            Resultado(dados = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inserir pergunta", e)
            Resultado(erro = "Não foi possível salvar a pergunta")
        }
    }

    suspend fun atualizar(pergunta: Pergunta): Resultado<Unit> {
        return try {
            dao.atualizar(pergunta)
            Resultado(dados = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao atualizar pergunta ${pergunta.id}", e)
            Resultado(erro = "Não foi possível salvar as alterações")
        }
    }

    suspend fun deletar(pergunta: Pergunta): Resultado<Unit> {
        return try {
            dao.deletar(pergunta)
            Resultado(dados = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao remover pergunta ${pergunta.id}", e)
            Resultado(erro = "Não foi possível remover a pergunta")
        }
    }

    /** Se o banco ainda não tem perguntas (primeira abertura), coloca as iniciais. */
    suspend fun popularSeVazio() {
        try {
            if (dao.contar() == 0) dao.inserirTodas(PERGUNTAS_INICIAIS)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao colocar as perguntas iniciais", e)
        }
    }
}
