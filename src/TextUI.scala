import Types._
import Utils._
import BoardType._

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object TextUI {
  val (boardWidth, boardHeight) = getBoardSize()
  val (correctWord, incorrectWord) = (500, -100)

  @tailrec
  def getBoardSize(asked: Boolean = false): (Int, Int) = {
    if (asked) println("Tamanho inválido! A altura e o comprimento do tabuleiro devem ser maiores ou iguais a 5.")
    val boardWidth = getInput("Insira o comprimento do tabuleiro").toInt
    val boardHeight = getInput("Insira a altura do tabuleiro").toInt

    if (boardHeight < 5 || boardWidth < 5) getBoardSize(true)
    else (boardWidth, boardHeight)
  }

  def mainMenu(): Unit = {
    println("Menu do jogo das palavras cruzadas!")
    println("1. Selecionar palavra")
    println("2. Reiniciar jogo")
    println("3. Alterar cor do texto")
    println("4. Sair")
  }

  def getInput(prompt: String): String = {
    print(prompt + ": ")
    readLine().trim
  }

  def selectWord(board: Board, words: List[String]): Option[String] = {
    val word = getInput("Digite a palavra que deseja selecionar").toUpperCase()
    val start = getInput("Digite a coordenada inicial no formato \"row,col\"").split(",")
    val direction = getInput("Digite a direção (North, South, East, West, NorthEast, NorthWest, SouthEast, SouthWest)")
    val startCoord = (start.head.toInt, start.last.toInt)
    val directionEnum = Direction.withName(direction)

    if (ZigZag.play(board, word, words, startCoord, directionEnum, Text)) Some(word)
    else None
  }

  def main(args: Array[String]): Unit = {
    def runGame(game: (Board, List[String], List[List[Coord2D]]), points: Int, startTime: Long, checkedWords: List[String]): Unit = {
      val board = game._1
      val words = game._2
      val positions = game._3
      println("Pontuação: " + points)
      mainMenu()
      getInput("Escolha uma opção") match {
        case "1" =>
          printBoard(board)
          val selectedWord = selectWord(board, words)
          if (selectedWord.isDefined) {
            val word = selectedWord.get
            if (ZigZag.isGameOver(startTime)) {
              println("Acabou o tempo!")
              println("Pontuação final: " + points)
            } else if (!checkedWords.contains(word)) {
              println("A palavra está correta!")
              runGame(game, points + correctWord, startTime, checkedWords :+ word) // Continue running the game with the same board
            } else {
              println("A palavra já foi selecionada!")
              runGame(game, points, startTime, checkedWords) // Continue running the game with the same board
            }
          } else {
            if (ZigZag.isGameOver(startTime)) {
              println("Acabou o tempo!")
              println("Pontuação final: " + points)
            } else {
              println("A palavra está incorreta!")
              runGame(game, points + incorrectWord, startTime, checkedWords) // Continue running the game with the same board
            }
          }
        case "2" =>
          println(Console.RESET)
          println("Jogo reiniciado!")
          printBoard(board)
          runGame(game, 0, startTime = System.currentTimeMillis(), List()) // Continue running the game with the same board
        case "3" =>
          println("Escolha uma das cores abaixo")
          println("1. Preto")
          println("2. Vermelho")
          println("3. Verde")
          println("4. Amarelo")
          println("5. Azul")
          println("6. Roxo")
          println("7. Ciano")
          println("8. Branco")
          println("9. Reset")
          getInput("Escolha uma cor") match {
            case "1" =>
              println(Console.BLACK)
            case "2" =>
              println(Console.RED)
            case "3" =>
              println(Console.GREEN)
            case "4" =>
              println(Console.YELLOW)
            case "5" =>
              println(Console.BLUE)
            case "6" =>
              println(Console.MAGENTA)
            case "7" =>
              println(Console.CYAN)
            case "8" =>
              println(Console.WHITE)
            case "9" =>
              println(Console.RESET)
            case _ =>
              println("Cor inválida")
          }
          runGame(game, points, startTime, checkedWords) // Continue running the game with the same board
        case "4" =>
          println("Obrigado por jogar!")
        case _ =>
          println("Opção inválida")
          runGame(game, points, startTime, checkedWords) // Continue running the game with the same board
      }
    }
    runGame(startGame(boardWidth, boardHeight, Text), 0, System.currentTimeMillis(), List()) // Start running the game
  }
}