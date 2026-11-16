package br.com.quizup.data.local

/*
 * Conteúdo que entra no banco quando ele ainda está vazio (primeira vez que o app abre).
 * correta: 0 = A, 1 = B, 2 = C, 3 = D
 */

val PERGUNTAS_INICIAIS = listOf(
    // ---------- FÁCIL ----------
    Pergunta(enunciado = "Quantos jogadores cada time tem em campo numa partida de futebol?",
        opcaoA = "9", opcaoB = "10", opcaoC = "11", opcaoD = "12", correta = 2, dificuldade = "FACIL"),
    Pergunta(enunciado = "Quanto tempo dura uma partida oficial, sem os acréscimos?",
        opcaoA = "60 minutos", opcaoB = "90 minutos", opcaoC = "80 minutos", opcaoD = "120 minutos", correta = 1, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual cartão expulsa o jogador de campo?",
        opcaoA = "Amarelo", opcaoB = "Verde", opcaoC = "Azul", opcaoD = "Vermelho", correta = 3, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual jogador brasileiro ficou conhecido como \"Rei do Futebol\"?",
        opcaoA = "Pelé", opcaoB = "Ronaldo", opcaoC = "Zico", opcaoD = "Romário", correta = 0, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual é o apelido da seleção brasileira?",
        opcaoA = "Albiceleste", opcaoB = "Canarinho", opcaoC = "Azzurra", opcaoD = "La Roja", correta = 1, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual jogador pode usar as mãos dentro da própria área?",
        opcaoA = "Zagueiro", opcaoB = "Lateral", opcaoC = "Goleiro", opcaoD = "Volante", correta = 2, dificuldade = "FACIL"),
    Pergunta(enunciado = "Como se chama o gol que o jogador marca contra o próprio time?",
        opcaoA = "Gol olímpico", opcaoB = "Gol de placa", opcaoC = "Gol de ouro", opcaoD = "Gol contra", correta = 3, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual país sediou a Copa do Mundo de 2014?",
        opcaoA = "Brasil", opcaoB = "Argentina", opcaoC = "Alemanha", opcaoD = "África do Sul", correta = 0, dificuldade = "FACIL"),
    Pergunta(enunciado = "Quantos pontos vale uma vitória no Campeonato Brasileiro?",
        opcaoA = "2", opcaoB = "3", opcaoC = "1", opcaoD = "4", correta = 1, dificuldade = "FACIL"),
    Pergunta(enunciado = "Qual estádio do Rio de Janeiro recebeu as finais das Copas de 1950 e 2014?",
        opcaoA = "Morumbi", opcaoB = "Mineirão", opcaoC = "Maracanã", opcaoD = "Arena da Baixada", correta = 2, dificuldade = "FACIL"),

    // ---------- MÉDIO ----------
    Pergunta(enunciado = "Em que ano o Brasil conquistou sua primeira Copa do Mundo?",
        opcaoA = "1950", opcaoB = "1962", opcaoC = "1970", opcaoD = "1958", correta = 3, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Qual seleção venceu a Copa do Mundo de 2002?",
        opcaoA = "Brasil", opcaoB = "Alemanha", opcaoC = "França", opcaoD = "Itália", correta = 0, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Quem marcou os dois gols do Brasil na final da Copa de 2002?",
        opcaoA = "Rivaldo", opcaoB = "Ronaldo", opcaoC = "Ronaldinho Gaúcho", opcaoD = "Roberto Carlos", correta = 1, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Em qual país foi disputada a primeira Copa do Mundo, em 1930?",
        opcaoA = "Brasil", opcaoB = "Itália", opcaoC = "Uruguai", opcaoD = "Argentina", correta = 2, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Qual time paranaense foi campeão brasileiro em 2001?",
        opcaoA = "Coritiba", opcaoB = "Paraná Clube", opcaoC = "Londrina", opcaoD = "Athletico Paranaense", correta = 3, dificuldade = "MEDIO"),
    Pergunta(enunciado = "O que é um \"gol olímpico\"?",
        opcaoA = "Gol direto de escanteio", opcaoB = "Gol de bicicleta", opcaoC = "Gol do meio de campo", opcaoD = "Gol de pênalti", correta = 0, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Qual seleção é conhecida como \"Albiceleste\"?",
        opcaoA = "Uruguai", opcaoB = "Argentina", opcaoC = "Itália", opcaoD = "Chile", correta = 1, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Quantas substituições cada time pode fazer numa partida oficial (sem prorrogação)?",
        opcaoA = "3", opcaoB = "4", opcaoC = "5", opcaoD = "6", correta = 2, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Qual país venceu a Copa do Mundo de 2010?",
        opcaoA = "Holanda", opcaoB = "Alemanha", opcaoC = "Brasil", opcaoD = "Espanha", correta = 3, dificuldade = "MEDIO"),
    Pergunta(enunciado = "Qual jogador brasileiro ficou conhecido como \"Fenômeno\"?",
        opcaoA = "Ronaldo Nazário", opcaoB = "Romário", opcaoC = "Adriano", opcaoD = "Rivaldo", correta = 0, dificuldade = "MEDIO"),

    // ---------- DIFÍCIL ----------
    Pergunta(enunciado = "Qual seleção venceu o Brasil por 7 a 1 na Copa de 2014?",
        opcaoA = "Holanda", opcaoB = "Alemanha", opcaoC = "Argentina", opcaoD = "Espanha", correta = 1, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual seleção venceu o Brasil na decisão de 1950, o \"Maracanaço\"?",
        opcaoA = "Argentina", opcaoB = "Itália", opcaoC = "Uruguai", opcaoD = "Suécia", correta = 2, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual clube revelou Neymar?",
        opcaoA = "Corinthians", opcaoB = "Flamengo", opcaoC = "São Paulo", opcaoD = "Santos", correta = 3, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Em que ano o Brasil conquistou o tricampeonato mundial, no México?",
        opcaoA = "1970", opcaoB = "1966", opcaoC = "1974", opcaoD = "1962", correta = 0, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Quem era o técnico da seleção brasileira no penta, em 2002?",
        opcaoA = "Carlos Alberto Parreira", opcaoB = "Luiz Felipe Scolari", opcaoC = "Zagallo", opcaoD = "Tite", correta = 1, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual país sediou a Copa do Mundo de 1994?",
        opcaoA = "França", opcaoB = "México", opcaoC = "Estados Unidos", opcaoD = "Itália", correta = 2, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual jogador brasileiro venceu a Bola de Ouro em 2007?",
        opcaoA = "Ronaldinho Gaúcho", opcaoB = "Ronaldo", opcaoC = "Rivaldo", opcaoD = "Kaká", correta = 3, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual é o número mínimo de jogadores de um time para a partida continuar?",
        opcaoA = "7", opcaoB = "6", opcaoC = "8", opcaoD = "9", correta = 0, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Qual regra pune o atacante à frente do penúltimo defensor no momento do passe?",
        opcaoA = "Falta", opcaoB = "Impedimento", opcaoC = "Lei da vantagem", opcaoD = "Tiro livre indireto", correta = 1, dificuldade = "DIFICIL"),
    Pergunta(enunciado = "Em qual cidade fica a sede da FIFA?",
        opcaoA = "Paris", opcaoB = "Londres", opcaoC = "Zurique", opcaoD = "Genebra", correta = 2, dificuldade = "DIFICIL"),

    // ---------- EXPERT ----------
    Pergunta(enunciado = "Qual jogador italiano perdeu o último pênalti na final da Copa de 1994?",
        opcaoA = "Franco Baresi", opcaoB = "Daniele Massaro", opcaoC = "Paolo Maldini", opcaoD = "Roberto Baggio", correta = 3, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Em que ano a FIFA foi fundada?",
        opcaoA = "1904", opcaoB = "1930", opcaoC = "1920", opcaoD = "1898", correta = 0, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Quem foi o artilheiro da Copa de 2002, com 8 gols?",
        opcaoA = "Rivaldo", opcaoB = "Ronaldo", opcaoC = "Miroslav Klose", opcaoD = "Christian Vieri", correta = 1, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Qual era o apelido de Garrincha?",
        opcaoA = "Galinho de Quintino", opcaoB = "Furacão da Copa", opcaoC = "Anjo de Pernas Tortas", opcaoD = "Doutor", correta = 2, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Qual seleção o Brasil venceu na final da Copa de 1970?",
        opcaoA = "Alemanha", opcaoB = "Inglaterra", opcaoC = "Uruguai", opcaoD = "Itália", correta = 3, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Qual foi o placar da final da Copa de 1958, Brasil x Suécia?",
        opcaoA = "5 a 2", opcaoB = "3 a 1", opcaoC = "4 a 1", opcaoD = "2 a 0", correta = 0, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Quem marcou o gol do título da Espanha na final da Copa de 2010?",
        opcaoA = "David Villa", opcaoB = "Andrés Iniesta", opcaoC = "Xavi", opcaoD = "Fernando Torres", correta = 1, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Quantos gols Pelé marcou em Copas do Mundo?",
        opcaoA = "10", opcaoB = "14", opcaoC = "12", opcaoD = "8", correta = 2, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Em que ano o Athletico Paranaense venceu a Copa Sul-Americana pela primeira vez?",
        opcaoA = "2016", opcaoB = "2019", opcaoC = "2021", opcaoD = "2018", correta = 3, dificuldade = "EXPERT"),
    Pergunta(enunciado = "Qual país sediou a Copa de 2002 junto com o Japão?",
        opcaoA = "Coreia do Sul", opcaoB = "China", opcaoC = "Austrália", opcaoD = "Catar", correta = 0, dificuldade = "EXPERT")
)

/** Ranking falso: 5 jogadores em cada nível para o jogo já começar com adversários. */
val RANKING_FALSO = listOf(
    Pontuacao(nome = "Ana", pontos = 100, acertos = 10, total = 10, dificuldade = "FACIL"),
    Pontuacao(nome = "Bruno", pontos = 90, acertos = 9, total = 10, dificuldade = "FACIL"),
    Pontuacao(nome = "Carla", pontos = 70, acertos = 7, total = 10, dificuldade = "FACIL"),
    Pontuacao(nome = "Diego", pontos = 60, acertos = 6, total = 10, dificuldade = "FACIL"),
    Pontuacao(nome = "Fernanda", pontos = 40, acertos = 4, total = 10, dificuldade = "FACIL"),

    Pontuacao(nome = "Gustavo", pontos = 180, acertos = 9, total = 10, dificuldade = "MEDIO"),
    Pontuacao(nome = "Helena", pontos = 160, acertos = 8, total = 10, dificuldade = "MEDIO"),
    Pontuacao(nome = "Igor", pontos = 120, acertos = 6, total = 10, dificuldade = "MEDIO"),
    Pontuacao(nome = "Júlia", pontos = 100, acertos = 5, total = 10, dificuldade = "MEDIO"),
    Pontuacao(nome = "Lucas", pontos = 60, acertos = 3, total = 10, dificuldade = "MEDIO"),

    Pontuacao(nome = "Marina", pontos = 240, acertos = 8, total = 10, dificuldade = "DIFICIL"),
    Pontuacao(nome = "Nicolas", pontos = 210, acertos = 7, total = 10, dificuldade = "DIFICIL"),
    Pontuacao(nome = "Olívia", pontos = 180, acertos = 6, total = 10, dificuldade = "DIFICIL"),
    Pontuacao(nome = "Pedro", pontos = 120, acertos = 4, total = 10, dificuldade = "DIFICIL"),
    Pontuacao(nome = "Rafaela", pontos = 90, acertos = 3, total = 10, dificuldade = "DIFICIL"),

    Pontuacao(nome = "Sofia", pontos = 350, acertos = 7, total = 10, dificuldade = "EXPERT"),
    Pontuacao(nome = "Thiago", pontos = 300, acertos = 6, total = 10, dificuldade = "EXPERT"),
    Pontuacao(nome = "Valentina", pontos = 250, acertos = 5, total = 10, dificuldade = "EXPERT"),
    Pontuacao(nome = "Vinícius", pontos = 200, acertos = 4, total = 10, dificuldade = "EXPERT"),
    Pontuacao(nome = "Yasmin", pontos = 150, acertos = 3, total = 10, dificuldade = "EXPERT")
)
