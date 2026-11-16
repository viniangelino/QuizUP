package br.com.quizup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.quizup.data.local.QuizDatabase
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.data.repository.RankingRepository
import br.com.quizup.data.repository.TimeRepository
import br.com.quizup.data.remote.RetrofitClient
import br.com.quizup.navigation.AppNavigation
import br.com.quizup.ui.theme.QuizTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Monta as camadas: Database (Singleton) -> DAO -> Repository, e Retrofit -> Repository
        val db = QuizDatabase.getDatabase(this)
        val perguntaRepository = PerguntaRepository(db.perguntaDao())
        val rankingRepository = RankingRepository(db.pontuacaoDao())
        val timeRepository = TimeRepository(RetrofitClient.api)   // API externa

        // Primeira vez que o app abre: coloca as perguntas e o ranking falso no banco
        CoroutineScope(Dispatchers.IO).launch {
            perguntaRepository.popularSeVazio()
            rankingRepository.popularSeVazio()
        }

        setContent {
            QuizTheme {
                AppNavigation(perguntaRepository, rankingRepository, timeRepository)
            }
        }
    }
}
