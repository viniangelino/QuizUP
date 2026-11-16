package br.com.quizup.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.quizup.data.local.NIVEIS
import br.com.quizup.data.local.Nivel
import br.com.quizup.data.local.PERGUNTAS_POR_JOGO
import br.com.quizup.navigation.Rotas
import br.com.quizup.ui.componentes.*
import br.com.quizup.ui.theme.AzulNoite

// ======================= INÍCIO (LOGO) =======================

@Composable
fun InicioScreen(navController: NavController) {
    Scaffold(containerColor = AzulNoite) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuizLogo(160.dp)
            Spacer(Modifier.height(24.dp))
            NomeApp()
            Text("Quanto você sabe de futebol?", color = Color.White, fontSize = 16.sp)
            Spacer(Modifier.height(48.dp))
            BotaoPrincipal(
                texto = "JOGAR",
                onClick = {
                    // Tira a tela de início da pilha: o "voltar" não retorna para ela
                    navController.navigate(Rotas.DIFICULDADE) { popUpTo(Rotas.INICIO) { inclusive = true } }
                }
            )
        }
    }
}

// ======================= DIFICULDADE =======================

@Composable
fun DificuldadeScreen(navController: NavController) {
    Scaffold(containerColor = AzulNoite) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuizLogo(96.dp)
            NomeApp()

            Painel(titulo = "NOVO JOGO") {
                Text("Selecione uma dificuldade para começar a jogar!", color = Color.White)
                NIVEIS.forEach { nivel ->
                    BotaoNivel(nivel) { navController.navigate(Rotas.explicacao(nivel.codigo)) }
                }
            }
        }
    }
}

// ======================= EXPLICAÇÃO RÁPIDA =======================

@Composable
fun ExplicacaoScreen(navController: NavController, nivel: Nivel) {
    // Único campo da tela: o nome que vai para o ranking
    var nome by remember { mutableStateOf("") }

    Scaffold(
        containerColor = AzulNoite,
        topBar = { BarraTopo("Como jogar", onVoltar = { navController.popBackStack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Painel(titulo = "NÍVEL ${nivel.nome.uppercase()}") {
                Text("⚽ $PERGUNTAS_POR_JOGO perguntas sobre futebol, cada uma com 4 alternativas.", color = Color.White)
                Text("✅ Cada resposta certa vale ${nivel.pontos} pontos.", color = Color.White)
                Text("❌ Resposta errada não vale ponto.", color = Color.White)
                Text("🏆 No fim, sua pontuação entra no ranking do nível ${nivel.nome}.", color = Color.White)
            }

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Seu nome no ranking") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            BotaoPrincipal(
                texto = "COMEÇAR",
                habilitado = nome.isNotBlank(),
                onClick = { navController.navigate(Rotas.jogo(nivel.codigo, nome.trim())) }
            )
        }
    }
}
