package br.com.quizup.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.quizup.data.local.buscarNivel
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.data.repository.RankingRepository
import br.com.quizup.data.repository.TimeRepository
import br.com.quizup.ui.telas.*
import br.com.quizup.ui.theme.Amarelo
import br.com.quizup.ui.theme.AzulBotao
import br.com.quizup.ui.theme.AzulNoite
import br.com.quizup.viewmodel.SessaoViewModel

/** Uma aba da barra de baixo. */
data class Aba(val titulo: String, val icone: ImageVector, val destino: String, val rotaDaTela: String)

@Composable
fun AppNavigation(
    perguntaRepository: PerguntaRepository,
    rankingRepository: RankingRepository,
    timeRepository: TimeRepository
) {
    val navController = rememberNavController()

    // Perfil (Jogador x Admin): um só ViewModel para o app inteiro
    val sessaoVm: SessaoViewModel = viewModel()
    val sessao by sessaoVm.uiState.collectAsStateWithLifecycle()

    // A aba "Perguntas" só existe para o administrador
    val abas = mutableListOf(
        Aba("Jogar", Icons.Default.Home, Rotas.DIFICULDADE, Rotas.DIFICULDADE),
        Aba("Ranking", Icons.Default.Star, Rotas.ranking("FACIL", -1), Rotas.RANKING),
        Aba("Times", Icons.Default.Search, Rotas.TIMES, Rotas.TIMES)
    )
    if (sessao.ehAdmin) {
        abas.add(Aba("Perguntas", Icons.Default.Edit, Rotas.ADM_PAINEL, Rotas.ADM_PAINEL))
    }
    abas.add(Aba("Perfil", Icons.Default.Person, Rotas.PERFIL, Rotas.PERFIL))

    // Rota da tela aberta agora: decide se mostra a barra e qual aba fica acesa
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route
    val mostrarBarra = abas.any { it.rotaDaTela == rotaAtual }

    Scaffold(
        containerColor = AzulNoite,
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = AzulBotao) {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = aba.rotaDaTela == rotaAtual,
                            onClick = { irParaAba(navController, aba.destino) },
                            icon = { Icon(aba.icone, contentDescription = aba.titulo) },
                            label = { Text(aba.titulo) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Amarelo)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.INICIO) { InicioScreen(navController) }

            composable(Rotas.DIFICULDADE) { DificuldadeScreen(navController) }

            composable(Rotas.EXPLICACAO) { backStackEntry ->
                val nivel = buscarNivel(backStackEntry.arguments?.getString("nivel"))
                ExplicacaoScreen(navController, nivel)
            }

            composable(Rotas.JOGO) { backStackEntry ->
                val nivel = buscarNivel(backStackEntry.arguments?.getString("nivel"))
                val nome = backStackEntry.arguments?.getString("nome") ?: "Jogador"
                JogoScreen(navController, perguntaRepository, rankingRepository, nivel, nome)
            }

            composable(Rotas.RANKING) { backStackEntry ->
                val nivel = backStackEntry.arguments?.getString("nivel") ?: "FACIL"
                val id = backStackEntry.arguments?.getString("pontuacaoId")?.toIntOrNull() ?: -1
                RankingScreen(navController, rankingRepository, nivel, id)
            }

            composable(Rotas.TIMES) { TimesScreen(timeRepository) }

            composable(Rotas.PERFIL) { PerfilScreen(navController, sessaoVm) }

            // ---------- Só para o administrador ----------
            composable(Rotas.ADM_PAINEL) {
                AdmPainelScreen(navController, perguntaRepository, sessao.ehAdmin)
            }

            composable(Rotas.ADM_PERGUNTA) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
                AdmPerguntaScreen(navController, perguntaRepository, id)
            }
        }
    }
}

/** Troca de aba sem empilhar telas repetidas (igual ao Trabalho 2). */
fun irParaAba(navController: NavController, rota: String) {
    navController.navigate(rota) {
        popUpTo(Rotas.DIFICULDADE) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
