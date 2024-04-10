import Types._
import Utils._

import scala.io.StdIn.readLine

object TextUI {
  def mainMenu(): Unit = {
    println("Bem-vindo ao jogo de palavras cruzadas!")
    println("1. Iniciar novo jogo")
    println("2. Selecionar palavra")
    println("3. Reiniciar jogo")
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
    def runGame(board: Option[Board] = None): Unit = {
      mainMenu()
      getInput("Escolha uma opção") match {
        case "1" =>
          runGame(Some(startGame())) // Continue running the game with the new board
        case "2" =>
          board match {
            case Some(b) =>
              selectWord()
              runGame(Some(b)) // Continue running the game with the same board
            case _ =>
              println("Erro: Não há jogo para selecionar uma palavra!")
              runGame()
              }
        case "3" =>
          board match {
            case Some(b) =>
              println("Jogo reiniciado!")
              printBoard(b)
              runGame(Some(b)) // Continue running the game with the same board
            case _ =>
              println("Erro: Não há jogo para reiniciar!")
              runGame()
          }
        case "4" =>
          println("Obrigado por jogar!")
        case _ =>
          println("Opção inválida")
          runGame(board) // Continue running the game with the same board
      }
    }
    runGame(Some(startGame())) // Start running the game
  }
}