import Types._

import scala.annotation.tailrec
import scala.io.Source

object ZigZag {
  // T1
  def randomChar(rand: MyRandom): (Char, MyRandom) = {
    val (n, nextRand) = rand.nextInt
    val c = (n.abs % 26 + 'A'.toInt).toChar
    (c, nextRand)
  }

  // T2
  def fillOneCell(board: Board, letter: Char, coord: Coord2D): Board = {
    val (row, col) = coord
    board.updated(row, board(row).updated(col, letter))
  }

  // T3
  @tailrec
  def setBoardWithWords(board: Board, words: List[String], positions: List[List[Coord2D]]): Board = {
    @tailrec
    def setWord(board: Board, word: String, position: List[Coord2D]): Board = {
      position match {
        case Nil => board
        case (row, col) :: tail =>
          val newBoard = fillOneCell(board, word.head, (row, col))
          setWord(newBoard, word.tail, tail)
      }
    }

    words match {
      case Nil => board
      case word :: wordTail =>
        positions match {
          case Nil => board
          case position :: positionTail =>
            val newBoard = setWord(board, word, position)
            setBoardWithWords(newBoard, wordTail, positionTail)
        }
    }
  }

  // T4
  def completeBoardRandomly(board: Board, r: MyRandom, f: MyRandom => (Char, MyRandom)): (Board, MyRandom) = {
    def fillRow(row: List[Char], r: MyRandom): (List[Char], MyRandom) = row match {
      case Nil => (Nil, r)
      case '-' :: tail =>
        val (char, nextRand) = f(r)
        val (newRow, finalRand) = fillRow(tail, nextRand)
        (char :: newRow, finalRand)
      case head :: tail =>
        val (newRow, nextRand) = fillRow(tail, r)
        (head :: newRow, nextRand)
    }

    def fillBoard(board: Board, r: MyRandom): (Board, MyRandom) = board match {
      case Nil => (Nil, r)
      case row :: tail =>
        val (newRow, nextRand) = fillRow(row, r)
        val (newBoard, finalRand) = fillBoard(tail, nextRand)
        (newRow :: newBoard, finalRand)
    }

    fillBoard(board, r)
  }

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

  def main(args: Array[String]): Unit = {
    val board: Board = List.fill(5)(List.fill(5)('-'))

    // Lê as palavras e posições do arquivo words.txt
    val (words, positions) = ZigZag.readWordsAndPositions("words.txt")

    val newBoard = ZigZag.setBoardWithWords(board, words, positions)

    val (randomizedBoard, _) = ZigZag.completeBoardRandomly(newBoard, MyRandom(9569784373L), ZigZag.randomChar)

    // Imprimindo o tabuleiro resultante
    println("Tabuleiro final:")
    randomizedBoard.foreach(row => println(row.mkString(" ")))
  }


}