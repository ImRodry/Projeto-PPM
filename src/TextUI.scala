import Types._
import Utils._

import scala.io.StdIn.readLine

object TextUI {
  def mainMenu(): Unit = {
    println("Bem-vindo ao jogo de palavras cruzadas!")
    println("1. Selecionar palavra")
    println("2. Reiniciar jogo")
    println("3. Alterar cor do texto")
    println("4. Sair")
  }

  def getInput(prompt: String): String = {
    print(prompt + ": ")
    readLine().trim
  }

  def selectWord(): Unit = {
    val word = getInput("Digite a palavra que deseja selecionar").toUpperCase()
    val start = getInput("Digite a coordenada inicial no formato \"row,col\"").split(",")
    val direction = getInput("Digite a direção (North, South, East, West, NorthEast, NorthWest, SouthEast, SouthWest)")

    val startCoord = (start.head.toInt, start.last.toInt)
    val directionEnum = Direction.withName(direction)

    if (ZigZag.play(word, startCoord, directionEnum)) {
      println("A palavra está correta!")
    } else {
      println("A palavra está incorreta!")
    }
  }

  def main(args: Array[String]): Unit = {
    def runGame(board: Board): Unit = {
      mainMenu()
      getInput("Escolha uma opção") match {
        case "1" =>
          selectWord()
          runGame(board) // Continue running the game with the same board
        case "2" =>
          println("Jogo reiniciado!")
          printBoard(board)
          runGame(board) // Continue running the game with the same board
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
          runGame(board) // Continue running the game with the same board
        case "4" =>
          println("Obrigado por jogar!")
        case _ =>
          println("Opção inválida")
          runGame(board) // Continue running the game with the same board
      }
    }
    runGame(startGame()) // Start running the game
  }
}