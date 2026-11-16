package br.com.quizup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.quizup.data.local.Nivel
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.data.repository.RankingRepository
import br.com.quizup.data.repository.TimeRepository

/*
 * O Android não sabe criar sozinho um ViewModel que recebe parâmetros no construtor.
 * Cada Factory injeta o Repository (e os dados que vieram pela rota) no ViewModel.
 */

class JogoViewModelFactory(
    private val perguntaRepository: PerguntaRepository,
    private val rankingRepository: RankingRepository,
    private val nivel: Nivel,
    private val nomeJogador: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JogoViewModel(perguntaRepository, rankingRepository, nivel, nomeJogador) as T
    }
}

class RankingViewModelFactory(
    private val repository: RankingRepository,
    private val nivel: String,
    private val pontuacaoId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RankingViewModel(repository, nivel, pontuacaoId) as T
    }
}

class AdmViewModelFactory(private val repository: PerguntaRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdmViewModel(repository) as T
    }
}

class TimesViewModelFactory(private val repository: TimeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimesViewModel(repository) as T
    }
}
