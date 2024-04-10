import Types._
import ZigZag._

import scala.util.Random

object Utils {
  // Função para imprimir o tabuleiro recursivamente
  def printBoard(board: Board): Unit = {
    def printRow(row: List[Char]): Unit = row match {
      case Nil => println() // Nova linha ao final de cada linha do tabuleiro
      case cell :: tail =>
        print(cell + " ") // Imprime o caractere da célula seguido de um espaço
        printRow(tail) // Chama recursivamente para a próxima célula na mesma linha
    }
    board match {
      case Nil => // Fim do tabuleiro, não faz nada
      case row :: tail =>
        printRow(row) // Imprime a linha atual do tabuleiro
        printBoard(tail) // Chama recursivamente para a próxima linha
    }
  }

  // Função para iniciar ou reiniciar o jogo
  def startGame(): Board   = {
    val board: Board = List.fill(5)(List.fill(5)('-')) // Tabuleiro vazio de 5x5
    val (words, positions) = readWordsAndPositions("words.txt") // Lê as palavras e posições do arquivo
    val newBoard = setBoardWithWords(board, words, positions) // Coloca as palavras no tabuleiro
    val (randomizedBoard, _) = completeBoardRandomly(newBoard, MyRandom(Random.nextInt()), randomChar) // Completa o tabuleiro com caracteres aleatórios
    println("Começou um jogo novo!")
    printBoard(randomizedBoard)
    randomizedBoard
  }
}
