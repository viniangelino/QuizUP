package br.com.quizup.data.repository

import android.util.Log
import br.com.quizup.data.remote.TimeDto
import br.com.quizup.data.remote.TimesApi
import java.io.IOException

/** Única porta de entrada para os dados da API externa. Só ele fala com o Retrofit. */
class TimeRepository(private val api: TimesApi) {

    suspend fun buscarTimes(nome: String): Resultado<List<TimeDto>> {
        return try {
            val resposta = api.buscarTimes(nome)
            Resultado(dados = resposta.teams ?: emptyList())   // null = não achou nenhum time
        } catch (e: IOException) {
            Log.e("TimeRepository", "Sem conexão", e)
            Resultado(erro = "Sem internet. Verifique a conexão e tente de novo.")
        } catch (e: Exception) {
            Log.e("TimeRepository", "Erro na API", e)
            Resultado(erro = "A API de times não respondeu. Tente mais tarde.")
        }
    }
}
