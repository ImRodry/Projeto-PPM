import Types._
import Types.Direction._

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

  //T5
  def play(word: String, start: Coord2D, direction: Direction): Boolean = {
    val (words, positions) = ZigZag.readWordsAndPositions("words.txt")
    val wordsAndPositions = words.zip(positions).toMap

    def getDirection(start: Coord2D, next: Coord2D): Direction = {
      val (startRow, startCol) = start
      val (nextRow, nextCol) = next

      (nextRow - startRow, nextCol - startCol) match {
        case (-1, 0) => North
        case (1, 0) => South
        case (0, 1) => East
        case (0, -1) => West
        case (-1, 1) => NorthEast
        case (-1, -1) => NorthWest
        case (1, 1) => SouthEast
        case (1, -1) => SouthWest
        case _ => throw new IllegalArgumentException("Invalid direction")
      }
    }

    wordsAndPositions.get(word) match {
      case Some(positions) =>
        val wordDirection = getDirection(positions.head, positions(1))
        positions.head == start && wordDirection == direction
      case _ => false
    }
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
}