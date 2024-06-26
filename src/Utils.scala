import Types._
import ZigZag._
import BoardType._

import scala.io.Source

object Utils {
  val (boardWidth, boardHeight) = (5, 5) // Largura e comprimento do tabuleiro da GUI

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
  def startGame(width: Int, height: Int, boardType: BoardType): (Board, List[String], List[List[Coord2D]])   = {
    val board: Board = List.fill(height)(List.fill(width)('-')) // Tabuleiro vazio de 5x5
    val (words, positions) = readWordsAndPositions("words.txt") // Lê as palavras e posições do arquivo
    val newBoard = setBoardWithWords(board, words, positions) // Coloca as palavras no tabuleiro
    val randomizedBoard = checkBoard(newBoard, words, boardType, MyRandom(1)) // Completa o tabuleiro com caracteres aleatórios
    if (boardType == BoardType.Text) { // Imprime o tabuleiro se for do tipo Text
      println("Começou um jogo novo!")
      printBoard(randomizedBoard)
    }
    (randomizedBoard, words, positions) // Retorna o tabuleiro e as palavras
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
  def inBounds(coord: Coord2D, boardType: BoardType): Boolean = {
    if (boardType == BoardType.Text)
      coord._1 >= 0 && coord._1 < TextUI.boardWidth && coord._2 >= 0 && coord._2 < TextUI.boardHeight
    else
      coord._1 >= 0 && coord._1 < Utils.boardWidth && coord._2 >= 0 && coord._2 < Utils.boardHeight
  }
}
