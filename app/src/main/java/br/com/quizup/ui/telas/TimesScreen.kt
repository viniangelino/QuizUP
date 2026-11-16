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
import br.com.quizup.data.repository.TimeRepository
import br.com.quizup.ui.componentes.BotaoPrincipal
import br.com.quizup.ui.theme.*
import br.com.quizup.viewmodel.TimesViewModel
import br.com.quizup.viewmodel.TimesViewModelFactory

/** Busca de times na API externa (Retrofit + TheSportsDB). */
@Composable
fun TimesScreen(
    repository: TimeRepository,
    vm: TimesViewModel = viewModel(factory = TimesViewModelFactory(repository))
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(containerColor = AzulNoite) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🔎 BUSCAR TIMES", color = Amarelo, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("Dados da internet (API TheSportsDB)", color = Color.White, fontSize = 13.sp)

            OutlinedTextField(
                value = estado.busca,
                onValueChange = { vm.alterarBusca(it) },
                label = { Text("Nome do time (ex.: Athletico, Flamengo)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            BotaoPrincipal(
                texto = if (estado.carregando) "BUSCANDO..." else "BUSCAR",
                onClick = { vm.buscar() },
                habilitado = !estado.carregando
            )

            // Filtro feito no ViewModel com filter { }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = estado.soBrasil, onCheckedChange = { vm.alternarSoBrasil(it) })
                Text("Só times do Brasil", color = Color.White)
            }

            if (estado.erro != null) {
                Text(estado.erro!!, color = Vermelho, fontWeight = FontWeight.Bold)
            } else if (estado.jaBuscou && estado.times.isEmpty()) {
                Text("Nenhum time encontrado.", color = Color.White)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(estado.times) { time ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AzulBotao)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(12.dp)) {
                            Text("⚽ ${time.nome}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("País: ${time.pais}", color = Color.White)
                            Text("Liga: ${time.liga}", color = Color.White)
                            Text("Estádio: ${time.estadio}", color = Color.White)
                            Text("Fundação: ${time.fundacao}", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
