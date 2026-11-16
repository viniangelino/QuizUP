package br.com.quizup.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.quizup.data.local.NIVEIS
import br.com.quizup.data.repository.RankingRepository
import br.com.quizup.navigation.Rotas
import br.com.quizup.ui.componentes.*
import br.com.quizup.ui.theme.*
import br.com.quizup.viewmodel.RankingViewModel
import br.com.quizup.viewmodel.RankingViewModelFactory

@Composable
fun RankingScreen(
    navController: NavController,
    repository: RankingRepository,
    nivel: String,
    pontuacaoId: Int,
    vm: RankingViewModel = viewModel(factory = RankingViewModelFactory(repository, nivel, pontuacaoId))
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()
    val minha = estado.minhaPontuacao

    Scaffold(containerColor = AzulNoite) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🏆 RANKING", color = Amarelo, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

            // Resultado de quem acabou de jogar
            if (minha != null) {
                Painel(titulo = "FIM DE JOGO!") {
                    Text("${minha.nome}, você fez", color = Color.White)
                    Text("${minha.pontos} pontos", color = Amarelo, fontSize = 32.sp, fontWeight = FontWeight.Black)
                    Text("${minha.acertos} de ${minha.total} acertos", color = Color.White)
                }
            }

            // Escolha do nível (o filtro é feito no RankingViewModel)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                NIVEIS.forEach { n ->
                    val escolhido = estado.nivel == n.codigo
                    Button(
                        onClick = { vm.escolherNivel(n.codigo) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (escolhido) Amarelo else AzulBotao,
                            contentColor = if (escolhido) TextoEscuro else Color.White
                        )
                    ) { Text(n.nome, fontSize = 13.sp) }
                }
            }

            if (estado.erro != null) {
                Text(estado.erro!!, color = Vermelho)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estado.linhas) { linha ->
                    val corTexto = if (linha.souEu) TextoEscuro else Color.White
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (linha.souEu) Amarelo else AzulBotao)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${linha.posicao}º", color = corTexto, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(48.dp))
                            Column(Modifier.weight(1f)) {
                                Text(if (linha.souEu) "${linha.nome} (você)" else linha.nome,
                                    color = corTexto, fontWeight = FontWeight.Bold)
                                Text("${linha.acertos}/${linha.total} acertos", color = corTexto, fontSize = 13.sp)
                            }
                            Text("${linha.pontos} pts", color = corTexto, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            // Acabou de jogar mas ficou fora do top 10
            if (minha != null && estado.nivel == minha.dificuldade && estado.linhas.none { it.souEu }) {
                Text("Você ficou fora do top 10. Tente de novo!", color = Color.White)
            }

            BotaoPrincipal(
                texto = "JOGAR",
                onClick = { navController.popBackStack(Rotas.DIFICULDADE, inclusive = false) }
            )
        }
    }
}
