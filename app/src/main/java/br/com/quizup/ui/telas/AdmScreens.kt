package br.com.quizup.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import br.com.quizup.data.local.NIVEIS
import br.com.quizup.data.local.alternativasDe
import br.com.quizup.data.local.buscarNivel
import br.com.quizup.data.repository.PerguntaRepository
import br.com.quizup.navigation.Rotas
import br.com.quizup.ui.componentes.*
import br.com.quizup.ui.theme.*
import br.com.quizup.viewmodel.AdmViewModel
import br.com.quizup.viewmodel.AdmViewModelFactory

private val LETRAS_ADM = listOf("A", "B", "C", "D")

// ======================= PAINEL DE PERGUNTAS (só ADM) =======================

@Composable
fun AdmPainelScreen(
    navController: NavController,
    repository: PerguntaRepository,
    ehAdmin: Boolean,
    vm: AdmViewModel = viewModel(factory = AdmViewModelFactory(repository))
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    MostrarMensagem(estado.mensagem) { vm.mensagemMostrada() }

    Scaffold(containerColor = AzulNoite) { innerPadding ->
        // Proteção extra: se não for ADM, não mostra nada do painel
        if (!ehAdmin) {
            Text(
                "Acesso só para administrador.\nEntre pela aba Perfil.",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(innerPadding).fillMaxWidth().padding(32.dp)
            )
            return@Scaffold
        }

        Column(Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
            Text("📝 PERGUNTAS DO BANCO", color = Amarelo, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))

            // ---------- Filtros (feitos no AdmViewModel com filter e sortedBy) ----------
            OutlinedTextField(
                value = estado.busca,
                onValueChange = { vm.alterarBusca(it) },
                label = { Text("Buscar pergunta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                BotaoFiltro("Todos", estado.filtroNivel == null, Modifier.weight(1f)) { vm.filtrarPorNivel(null) }
                NIVEIS.forEach { n ->
                    BotaoFiltro(n.nome, estado.filtroNivel == n.codigo, Modifier.weight(1f)) { vm.filtrarPorNivel(n.codigo) }
                }
            }
            Text(
                "${estado.perguntas.size} de ${estado.todas.size} perguntas · toque para editar",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estado.perguntas) { p ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AzulBotao),
                        modifier = Modifier.clickable { navController.navigate(Rotas.admPergunta(p.id)) }
                    ) {
                        Row(Modifier.fillMaxWidth().padding(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(buscarNivel(p.dificuldade).nome.uppercase(), color = Amarelo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(p.enunciado, color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Resposta: ${LETRAS_ADM[p.correta]}) ${alternativasDe(p)[p.correta]}", color = Color.White, fontSize = 13.sp)
                            }
                            // Lixeira: Delete no banco
                            IconButton(onClick = { vm.remover(p) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Vermelho)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            BotaoPrincipal(texto = "+ NOVA PERGUNTA", onClick = { navController.navigate(Rotas.admPergunta(-1)) })
        }
    }
}

@Composable
private fun BotaoFiltro(texto: String, escolhido: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (escolhido) Amarelo else AzulBotao,
            contentColor = if (escolhido) TextoEscuro else Color.White
        )
    ) { Text(texto, fontSize = 11.sp) }
}

// ======================= NOVA / EDITAR PERGUNTA =======================

/** id = -1: pergunta nova (Create). Outro id: editar essa pergunta (Update). */
@Composable
fun AdmPerguntaScreen(
    navController: NavController,
    repository: PerguntaRepository,
    id: Int,
    vm: AdmViewModel = viewModel(factory = AdmViewModelFactory(repository))
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()
    val editando = id != -1

    // Editando: pede ao ViewModel para buscar a pergunta no banco pelo id da rota
    LaunchedEffect(id) {
        if (editando) vm.carregarParaEditar(id)
    }

    MostrarMensagem(estado.mensagem) { vm.mensagemMostrada() }

    // Salvou no banco: volta para o painel (a lista do painel se atualiza sozinha pelo Flow)
    LaunchedEffect(estado.salvo) {
        if (estado.salvo) navController.popBackStack()
    }

    Scaffold(
        containerColor = AzulNoite,
        topBar = {
            BarraTopo(if (editando) "Editar pergunta" else "Nova pergunta", onVoltar = { navController.popBackStack() })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Dificuldade: 4 botões, o escolhido fica amarelo
            Text("Dificuldade", color = Color.White, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                NIVEIS.forEach { nivel ->
                    BotaoFiltro(nivel.nome, estado.dificuldade == nivel.codigo, Modifier.weight(1f)) {
                        vm.escolherDificuldade(nivel.codigo)
                    }
                }
            }

            OutlinedTextField(
                value = estado.enunciado,
                onValueChange = { vm.alterarEnunciado(it) },
                label = { Text("Pergunta") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            for (i in 0..3) {
                OutlinedTextField(
                    value = estado.opcoes[i],
                    onValueChange = { vm.alterarOpcao(i, it) },
                    label = { Text("Alternativa ${LETRAS_ADM[i]}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Qual alternativa é a certa: 4 botões A B C D
            Text("Alternativa correta", color = Color.White, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 0..3) {
                    Button(
                        onClick = { vm.escolherCorreta(i) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (estado.correta == i) Verde else AzulBotao
                        )
                    ) { Text(LETRAS_ADM[i], fontWeight = FontWeight.Bold) }
                }
            }

            if (estado.erro != null) {
                Text(estado.erro!!, color = Vermelho, fontWeight = FontWeight.Bold)
            }

            BotaoPrincipal(
                texto = if (editando) "SALVAR ALTERAÇÕES" else "SALVAR NO BANCO",
                onClick = { vm.salvar() }
            )
        }
    }
}
