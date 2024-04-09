import Types._

import scala.annotation.tailrec

object ZigZag {
  def randomChar(rand: MyRandom): (Char, MyRandom) = {
    val (n, nextRand) = rand.nextInt
    val c = (n % 26 + 'A'.toInt).toChar
    (c, nextRand)
  }

  def fillOneCell(board: Board, letter: Char, coord: Coord2D): Board = {
    val (row, col) = coord
    board.updated(row, board(row).updated(col, letter))
  }

  @tailrec
  def setBoardWithWords(board: Board, words: List[String], positions: List[List[Coord2D]]): Board = {
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

  def setWord(board: Board, word: String, position: List[Coord2D]): Board = {
    position match {
      case Nil => board
      case (row, col) :: tail =>
        val newBoard = board.updated(row, board(row).updated(col, word.head))
        setWord(newBoard, word.tail, tail)
    }
  }

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

  def main(args: Array[String]): Unit = {
    val board: Board = List.fill(5)(List.fill(5)('-'))
    val words = List("HELLO", "WORLD")
    val positions = List(List((0,0), (0,1), (0,2), (0,3), (0,4)), List((1,0), (1,1), (1,2), (1,3), (1,4)))

    val newBoard = ZigZag.setBoardWithWords(board, words, positions)

    newBoard.foreach(row => println(row.mkString(" ")))
  }
}