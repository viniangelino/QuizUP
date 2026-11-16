package br.com.quizup.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import br.com.quizup.navigation.Rotas
import br.com.quizup.navigation.irParaAba
import br.com.quizup.ui.componentes.*
import br.com.quizup.ui.theme.*
import br.com.quizup.viewmodel.SessaoViewModel

/**
 * Perfil: mostra se o usuário é Jogador ou Administrador.
 * Jogador vê o login de ADM. Administrador vê o atalho para as perguntas e o botão sair.
 */
@Composable
fun PerfilScreen(navController: NavController, vm: SessaoViewModel) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

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
            QuizLogo(88.dp)
            Text(
                if (estado.ehAdmin) "Você está como: ADMINISTRADOR" else "Você está como: JOGADOR",
                color = Amarelo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (estado.ehAdmin) {
                // ---------- Administrador ----------
                Text("A aba Perguntas apareceu na barra de baixo.", color = Color.White)
                BotaoPrincipal(texto = "GERENCIAR PERGUNTAS", onClick = { irParaAba(navController, Rotas.ADM_PAINEL) })
                OutlinedButton(onClick = { vm.sair() }, modifier = Modifier.fillMaxWidth()) {
                    Text("SAIR DO MODO ADM", color = Color.White)
                }
            } else {
                // ---------- Jogador: login de ADM ----------
                Text("Entre como administrador para cadastrar perguntas.", color = Color.White)
                OutlinedTextField(
                    value = estado.usuario,
                    onValueChange = { vm.alterarUsuario(it) },
                    label = { Text("Usuário") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = estado.senha,
                    onValueChange = { vm.alterarSenha(it) },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (estado.erro != null) {
                    Text(estado.erro!!, color = Vermelho)
                }
                BotaoPrincipal(texto = "ENTRAR COMO ADM", onClick = { vm.entrarComoAdmin() })
                Text("Acesso de teste: admin / admin", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}
