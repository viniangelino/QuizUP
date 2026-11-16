# QuizUP — Quiz de Futebol ⚽

![QuizUP](docs/logo/quizup-banner.png)

App Android em **Kotlin + Jetpack Compose** com perguntas sobre futebol em 4 níveis, ranking salvo no celular (**Room**),
busca de times numa API externa (**Retrofit**) e perfil **Jogador × Administrador**.

## Como rodar

1. Abra a pasta no **Android Studio Quail (2026.1) ou mais novo** (*File → Open*, a pasta que tem o `settings.gradle.kts`).
2. Se perguntar a JVM do Gradle, escolha **JVM 21**. Espere sincronizar.
3. Rode num emulador com **Android 8.0 (API 26)** ou superior **e com internet** (para a aba Times).

Na primeira vez que o app abre, o banco recebe **40 perguntas** (10 por nível) e um **ranking falso** (5 jogadores por nível).

**Login de administrador** (aba Perfil): usuário `admin` · senha `admin`

## Telas (9)

| # | Tela | O que faz |
|---|------|-----------|
| 1 | **Início** | Logo QuizUP + botão JOGAR |
| 2 | **Jogar** (aba) | Painel "NOVO JOGO": Fácil, Médio, Difícil, Expert |
| 3 | **Como jogar** | Explicação rápida + nome do jogador |
| 4 | **Pergunta** | 4 alternativas. Certa fica verde, errada vermelha → Próxima |
| 5 | **Ranking** (aba) | Resultado da partida + top 10 do nível escolhido |
| 6 | **Times** (aba) | Busca times na API TheSportsDB (Retrofit), com filtro "só do Brasil" |
| 7 | **Perfil** (aba) | Mostra se é Jogador ou Admin. Login e saída do modo ADM |
| 8 | **Perguntas** (aba, **só ADM**) | Lista do banco com busca, filtro por nível, editar e remover |
| 9 | **Nova / Editar pergunta** | Formulário: nível, pergunta, 4 alternativas e qual é a certa |

## Checklist do professor → onde está

### Arquitetura e Modularidade

| Requisito | Onde |
|---|---|
| **MVVM com 4 pacotes** (ui, data.local, data.repository, data.remote) | `ui/`, `data/local/`, `data/repository/`, `data/remote/` (+ `viewmodel/` e `navigation/`) |
| **Mínimo 2 ViewModels**, cada um com seu UiState | `JogoViewModel` (JogoUiState), `RankingViewModel` (RankingUiState), `AdmViewModel` (AdmUiState), `TimesViewModel` (TimesUiState), `SessaoViewModel` (SessaoUiState) |
| **ViewModelFactory** injetando o Repository | `viewmodel/ViewModelFactories.kt`: Jogo, Ranking, Adm e Times |
| **Tratamento de exceções no Repository**, com o erro chegando ao UiState | Todos os Repository têm `try-catch` (e `.catch` nos Flows) e devolvem `Resultado(erro = "...")`. O ViewModel copia para `uiState.erro` e a tela mostra. No `TimeRepository`, falta de internet (`IOException`) tem mensagem própria |

### Persistência e Dados

| Requisito | Onde |
|---|---|
| **Mínimo 2 @Entity** | `Pergunta` (dados principais) e `Pontuacao` (placar), em `data/local/Entidades.kt` |
| **Flow** coletado pelo ViewModel | `PerguntaDao.buscarTodas()` e `PontuacaoDao.buscarTodas()` → `collect` no `AdmViewModel` e no `RankingViewModel` |
| **CRUD completo** | **C**: nova pergunta e salvar pontuação · **R**: Flow do painel e do ranking · **U**: editar pergunta (`@Update`) · **D**: lixeira (`@Delete`) |
| **Retrofit** (API externa) | `data/remote/TimesApi.kt` (interface + `RetrofitClient`) → `TimeRepository` → `TimesViewModel` → aba Times |

### Interface e Lógica

| Requisito | Onde |
|---|---|
| **Mínimo 6 telas** | 9 telas (tabela acima) |
| **Bottom Navigation Bar** | `navigation/AppNavigation.kt`: `NavigationBar` com Jogar, Ranking, Times, (Perguntas) e Perfil |
| **map / filter / sortedBy com lambdas no ViewModel** | `RankingViewModel.montarTop10()`: `filter`, `sortedByDescending`, `mapIndexed` · `AdmViewModel.filtrar()`: `filter` por nível e texto, `sortedBy` · `TimesViewModel`: `map` (DTO → tela), `sortedBy`, `filter` (só Brasil) |
| **LazyColumn** | Ranking, Times e Perguntas |
| **Controle de perfil (Admin × Jogador)** | `SessaoViewModel.ehAdmin`: a aba **Perguntas** só aparece na barra quando é ADM, e o painel bloqueia se não for |

## Estrutura

```
app/src/main/java/br/com/quizup/
├── MainActivity.kt              # monta Database/Retrofit -> Repository e coloca os dados iniciais
├── data/
│   ├── local/                   # Room: Entidades, DAOs, QuizDatabase (Singleton), DadosIniciais
│   ├── remote/                  # Retrofit: TimesApi + RetrofitClient
│   └── repository/              # PerguntaRepository, RankingRepository, TimeRepository, Resultado
├── viewmodel/                   # ViewModels + UiStates + Factories
├── navigation/                  # Rotas + NavHost + NavigationBar
└── ui/                          # telas, componentes e tema
```

## Integrantes

- ✏️ Vinicius Vicente Angelino
- ✏️ Marcos Danrlei de Araujo
- ✏️ Luis Felipe de Paula Fogaça
- ✏️ Lucas Pereira Hartinger
- ✏️ Felipe Paulista Silveira
