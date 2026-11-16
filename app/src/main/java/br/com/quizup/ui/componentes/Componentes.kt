@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.quizup.ui.componentes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.quizup.data.local.Nivel
import br.com.quizup.ui.theme.*

/** Logo do QuizUP: quadrado azul com uma bola de futebol. */
@Composable
fun QuizLogo(tamanho: Dp = 120.dp) {
    Box(
        modifier = Modifier
            .size(tamanho)
            .clip(RoundedCornerShape(28.dp))
            .background(AzulPainel),
        contentAlignment = Alignment.Center
    ) {
        Text("⚽", fontSize = (tamanho.value / 2).sp)
    }
}

/** Nome do app: "Quiz" branco + "UP" amarelo. */
@Composable
fun NomeApp() {
    Row {
        Text("Quiz", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("UP", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Amarelo)
    }
}

/** Barra de topo com seta de voltar (mesma ideia da BarraTopo dos trabalhos anteriores). */
@Composable
fun BarraTopo(titulo: String, onVoltar: () -> Unit) {
    TopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onVoltar) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AzulNoite,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

/** Painel azul com título (como o "NOVO JOGO" da referência). */
@Composable
fun Painel(titulo: String, conteudo: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AzulPainel)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            conteudo()
        }
    }
}

/** 4 quadradinhos que mostram o nível (acesos conforme a dificuldade). */
@Composable
fun BarrasNivel(acesas: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        for (i in 1..4) {
            val cor = if (i <= acesas) Color.White else Color.White.copy(alpha = 0.25f)
            Box(Modifier.size(10.dp).background(cor))
        }
    }
}

/** Botão de nível: quadradinhos + nome + seta. */
@Composable
fun BotaoNivel(nivel: Nivel, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AzulBotao)
    ) {
        BarrasNivel(nivel.barras)
        Spacer(Modifier.width(16.dp))
        Text(nivel.nome, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("▶", fontSize = 18.sp)
    }
}

/** Botão amarelo de ação principal. */
@Composable
fun BotaoPrincipal(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Amarelo, contentColor = TextoEscuro)
    ) {
        Text(texto, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
    }
}

/** Mostra o Toast que o ViewModel colocou no UiState (igual ao Trabalho 4). */
@Composable
fun MostrarMensagem(mensagem: String?, onMostrada: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(mensagem) {
        if (mensagem != null) {
            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
            onMostrada()
        }
    }
}
