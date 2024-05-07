import Types._
import ZigZag._

import scala.io.Source
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
  def startGame(width: Int, length: Int): Board   = {
    val board: Board = List.fill(length)(List.fill(width)('-')) // Tabuleiro vazio de 5x5
    val (words, positions) = readWordsAndPositions("words.txt") // Lê as palavras e posições do arquivo
    val newBoard = setBoardWithWords(board, words, positions) // Coloca as palavras no tabuleiro
    val (randomizedBoard, _) = completeBoardRandomly(newBoard, MyRandom(Random.nextInt()), randomChar) // Completa o tabuleiro com caracteres aleatórios
    println("Começou um jogo novo!")
    printBoard(randomizedBoard)
    randomizedBoard
  }

  // Função para ler as palavras e posições do arquivo
  def readWordsAndPositions(filename: String): (List[String], List[List[Coord2D]]) = {
    val src = Source.fromFile(filename)
    val lines = src.getLines().toList
    src.close()
    val words = lines.zipWithIndex.collect { case (line, i) if i % 2 == 0 => line }
    val positions = lines.zipWithIndex.collect { case (line, i) if i % 2 != 0 =>
      line.split(" ").map(cord => {
        val Array(row, col) = cord.split(",").map(_.toInt)
        (row, col)
      }).toList
    }
    (words, positions)
  }

  // Função para verificar se uma coordenada está dentro dos limites do tabuleiro
  def inBounds(coord: Coord2D): Boolean = {
    coord._1 >= 0 && coord._1 < TextUI.boardWidth && coord._2 >= 0 && coord._2 < TextUI.boardLength
  }
}
