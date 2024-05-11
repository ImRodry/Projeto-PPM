import Types._
import Types.Direction._
import Utils._
import BoardType._

import scala.annotation.tailrec

object ZigZag {
  private val directions = Direction.values

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
    checkWord(board, word, start, direction, List(start), boardType)
  }

  def checkWord(board: Board, word: String, coord: Coord2D, nextDirection: Direction, checkedCoords: List[Coord2D], boardType: BoardType): Boolean = {
    if (word.isEmpty) true
    else {
      val (row, col) = coord
      if (board(row)(col) == word.head) {
        val nextCoord: Coord2D = nextDirection match {
          case North => (row - 1, col)
          case South => (row + 1, col)
          case East => (row, col + 1)
          case West => (row, col - 1)
          case NorthEast => (row - 1, col + 1)
          case NorthWest => (row - 1, col - 1)
          case SouthEast => (row + 1, col + 1)
          case SouthWest => (row + 1, col - 1)
        }
        if (!checkedCoords.contains(nextCoord) && inBounds(nextCoord, boardType))
          directions.exists(direction => checkWord(board, word.tail, nextCoord, direction, checkedCoords :+ nextCoord, boardType))
        else false
      } else false
    }
  }

  //T6
  def checkBoard(incompleteBoard: Board, words: List[String], boardType: BoardType, random: MyRandom): Board = {
    System.out.println("Checking if the board is valid")
    val (board, newRandom) = completeBoardRandomly(board, random, randomChar)
    val coords = for {
      x <- 0 to 4
      y <- 0 to 4
    } yield (x, y)

    // Check if all words exist once and only once
    val isValidBoard = words.forall { word =>
      // Count how many times checkWord is true for all coords
      val count = coords.flatMap { coord =>
        // And for all directions in each coord
        directions.map { direction =>
          checkWord(board, word, coord, direction, List.empty[Coord2D], boardType)
        }
      }.count(identity) // count how many times checkWord returns true for each word

      count == 1 // check if that value is more than 1 for each word
    }
    // If one of the words exists more than once, generate a new board from the newRandom
    if (!isValidBoard) checkBoard(incompleteBoard, words, boardType, newRandom)
    // Otherwise return the generated board
    else board
  }

  //T7
  def getElapsedTime(startTime: Long): Long = {
    System.currentTimeMillis() - startTime
  }

  def isGameOver(startTime: Long): Boolean = {
    getElapsedTime(startTime) > 30000
  }
}
