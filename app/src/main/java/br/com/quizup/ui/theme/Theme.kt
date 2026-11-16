package br.com.quizup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores do app (azul da tela "Novo Jogo" de referência + amarelo)
val AzulNoite = Color(0xFF0A1A3A)   // fundo das telas
val AzulPainel = Color(0xFF1E88E5)  // painéis
val AzulBotao = Color(0xFF155A8A)   // botões de nível e alternativas
val Amarelo = Color(0xFFFFC107)     // destaque / botão principal
val TextoEscuro = Color(0xFF2B1D00)
val Verde = Color(0xFF43A047)       // resposta certa
val Vermelho = Color(0xFFE53935)    // resposta errada

private val Cores = darkColorScheme(
    primary = AzulPainel,
    onPrimary = Color.White,
    secondary = Amarelo,
    onSecondary = TextoEscuro,
    background = AzulNoite,
    onBackground = Color.White,
    surface = AzulNoite,
    onSurface = Color.White,
    error = Vermelho
)

@Composable
fun QuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Cores, content = content)
}
