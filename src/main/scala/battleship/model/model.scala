package battleship.model

import Math._

case class Tile(x: Int, y: Int) {
  def < (t: Tile): Boolean = x < t.x || y < t.y
  def > (t: Tile): Boolean = x > t.x || y > t.y
}

trait OrthogonalSegment {
  
  protected def validate() = assert(a.x == b.x || a.y == b.y)
  
  def a: Tile
  def b: Tile
  
  lazy val leftBottom = if (a < b) a else b
  lazy val rightTop = if(b < a) a else b
  
  def size = abs(max(a.x - b.x, a.y - b.y))

  def contains(t: Tile): Boolean = (t.x >= leftBottom.x && t.x <= rightTop.x && t.y >= leftBottom.y && t.y <= rightTop.y)
  
}

case class Ship(a: Tile, b: Tile, shots: Seq[Tile] = Seq.empty[Tile]) extends OrthogonalSegment {
  validate()
  
  def remaining = size - shots.size
  def sunk: Boolean = remaining == 0
  def shoot(t: Tile): Ship = {
    if(shots.contains(t) || (!contains(t))) this
    else copy(a,b, shots :+ t)
  }
}

case class Board(playerName: String, ships: Iterable[Ship]) {
  def tilesRemaining = ships.foldLeft(0)(_ + _.remaining)
  def shipsRemaining = ships.filter(!_.sunk)
  def lost = tilesRemaining == 0
}

case class Game(gridSize: Int, playerA: Board, playerB: Board)