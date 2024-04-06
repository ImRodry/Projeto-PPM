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
}