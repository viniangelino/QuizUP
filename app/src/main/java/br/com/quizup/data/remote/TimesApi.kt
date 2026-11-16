package br.com.quizup.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Integração externa: API gratuita TheSportsDB (https://www.thesportsdb.com).
 * Exemplo de chamada: https://www.thesportsdb.com/api/v1/json/123/searchteams.php?t=Athletico
 * "123" é a chave pública gratuita da API.
 */

/** Um time como ele vem no JSON da API (os nomes dos campos têm que ser iguais aos do JSON). */
data class TimeDto(
    val idTeam: String?,
    val strTeam: String?,
    val strCountry: String?,
    val strLeague: String?,
    val strStadium: String?,
    val intFormedYear: String?
)

/** A API devolve { "teams": [ ... ] } ou { "teams": null } quando não acha nada. */
data class RespostaTimes(val teams: List<TimeDto>?)

/** O Retrofit cria a implementação desta interface sozinho. */
interface TimesApi {
    @GET("searchteams.php")
    suspend fun buscarTimes(@Query("t") nome: String): RespostaTimes
}

/** Um único Retrofit para o app inteiro (mesma ideia do Singleton do banco). */
object RetrofitClient {
    private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/123/"

    val api: TimesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())   // converte o JSON nas data classes acima
        .build()
        .create(TimesApi::class.java)
}
