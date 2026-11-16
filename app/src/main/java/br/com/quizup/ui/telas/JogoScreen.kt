package br.com.quizup.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.quizup.data.local.Nivel
import br.com.quizup.data.local.alternativasDe
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.data.repository.RankingRepository
import br.com.quizup.navigation.Rotas
import br.com.quizup.ui.componentes.*
import br.com.quizup.ui.theme.*
import br.com.quizup.viewmodel.JogoViewModel
import br.com.quizup.viewmodel.JogoViewModelFactory

private val LETRAS = listOf("A", "B", "C", "D")

@Composable
fun JogoScreen(
    navController: NavController,
    perguntaRepository: PerguntaRepository,
    rankingRepository: RankingRepository,
    nivel: Nivel,
    nome: String,
    vm: JogoViewModel = viewModel(factory = JogoViewModelFactory(perguntaRepository, rankingRepository, nivel, nome))
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    // Erro tratado no Repository chega pelo UiState e vira Toast
    MostrarMensagem(estado.erro) { vm.erroMostrado() }

    // Quando o ViewModel salva o resultado no banco, abre o ranking
    LaunchedEffect(estado.pontuacaoId) {
        val id = estado.pontuacaoId
        if (id != null) {
            navController.navigate(Rotas.ranking(nivel.codigo, id)) {
                popUpTo(Rotas.DIFICULDADE) // tira explicação e jogo da pilha
            }
        }
    }

    Scaffold(
        containerColor = AzulNoite,
        topBar = { BarraTopo("Nível ${nivel.nome}  ·  ${estado.pontos} pts", onVoltar = { navController.popBackStack() }) }
    ) { innerPadding ->
        if (estado.carregando) {
            Box(Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        if (estado.perguntas.isEmpty()) {
            Text(
                "Ainda não há perguntas neste nível. Peça para o ADM cadastrar!",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )
            return@Scaffold
        }

        val pergunta = estado.perguntas[estado.indice]
        val respondeu = estado.respostaEscolhida != null

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Pergunta ${estado.indice + 1} de ${estado.perguntas.size}", color = Color.White)

            Painel(titulo = "⚽") {
                Text(
                    pergunta.enunciado,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // As 4 alternativas
            alternativasDe(pergunta).forEachIndexed { i, texto ->
                // Antes de responder: azul. Depois: certa em verde, a errada escolhida em vermelho.
                val cor = if (!respondeu) AzulBotao
                else if (i == pergunta.correta) Verde
                else if (i == estado.respostaEscolhida) Vermelho
                else AzulBotao

                Button(
                    onClick = { vm.responder(i) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cor)
                ) {
                    Text("${LETRAS[i]})  $texto", fontSize = 17.sp, modifier = Modifier.fillMaxWidth())
                }
            }

            if (respondeu) {
                val acertou = estado.respostaEscolhida == pergunta.correta
                Text(
                    if (acertou) "✅ Acertou! +${nivel.pontos} pontos" else "❌ Errou! A certa era a ${LETRAS[pergunta.correta]}",
                    color = if (acertou) Verde else Vermelho,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                val ultima = estado.indice + 1 == estado.perguntas.size
                BotaoPrincipal(
                    texto = if (ultima) "VER RANKING" else "PRÓXIMA PERGUNTA",
                    onClick = { vm.proxima() }
                )
            }
        }
    }
}
