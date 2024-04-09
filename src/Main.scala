import Types.Board

object Main extends App {
  // Definindo uma instância de Random para teste
  val random = MyRandom(9569784373L) // Seed aleatória

  // Gerando uma lista de coordenadas para teste
  val positions = List(
    List((0, 0), (1, 1), (0, 2), (1, 3), (2, 4), (3, 5), (3, 6)),
    List((1, 6), (2, 6), (2, 7), (2, 8), (3, 8), (4, 8), (4, 9)),
    List((2, 0), (2, 1), (3, 1), (4, 1), (4, 2), (3, 2), (3, 3), (4, 4), (5, 4)),
    List((7, 1), (7, 2), (7, 3), (7, 4), (6, 5), (5, 6), (5, 7), (6, 6)),
    List((8, 6), (8, 7), (8, 8), (9, 8))
  )

  // Lendo as palavras e suas posições de um arquivo para teste
  val words = List(
    "PRODUTO",
    "MOCHILA",
    "CADERNETA",
    "PROGRAMA",
    "RATO",
  )

  // Criando um tabuleiro inicial vazio para teste
  val emptyBoard: Board = List.fill(10)(List.fill(10)('-'))

  // Preenchendo o tabuleiro com as palavras nas posições especificadas
  val boardWithWords = ZigZag.setBoardWithWords(emptyBoard, words, positions)

  // Completando o tabuleiro aleatoriamente
  val (randomizedBoard, _) = ZigZag.completeBoardRandomly(boardWithWords, random, ZigZag.randomChar)

  // Imprimindo o tabuleiro resultante
  println("Tabuleiro final:")
  randomizedBoard.foreach(row => println(row.mkString(" ")))
}