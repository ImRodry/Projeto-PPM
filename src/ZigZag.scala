import Types._
import Types.Direction._
import Utils._
import BoardType._

import scala.annotation.tailrec

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
  def play(board: Board, word: String, start: Coord2D, direction: Direction, boardType: BoardType): Boolean = {
    val directions = Direction.values
    System.out.println("Checking word: " + word + " at " + start)
    def checkWord(board: Board, word: String, coord: Coord2D, nextDirection: Direction): Boolean = {
      if (word.isEmpty) true
      else {
        val (row, col) = coord
        if (board(row)(col) == word.head) {
          val nextCoord = nextDirection match {
            case North => (row - 1, col)
            case South => (row + 1, col)
            case East => (row, col + 1)
            case West => (row, col - 1)
            case NorthEast => (row - 1, col + 1)
            case NorthWest => (row - 1, col - 1)
            case SouthEast => (row + 1, col + 1)
            case SouthWest => (row + 1, col - 1)
          }
          System.out.println("Checking letter: " + word.head + " at " + nextCoord + " with direction " + nextDirection)
          if (inBounds(nextCoord, boardType)) {
            directions.exists(direction => checkWord(board, word.tail, nextCoord, direction))
          }
          else false
        } else false
      }
    }
    checkWord(board, word, start, direction)
  }

  //T6
  
}