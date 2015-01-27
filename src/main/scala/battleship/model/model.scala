package battleship.model

import Math._

case class Tile(x: Int, y: Int) {
  def < (t: Tile): Boolean = x < t.x || y < t.y
  def > (t: Tile): Boolean = x > t.x || y > t.y
}

trait OrthogonalSegment {
  
  protected def validate = assert(a.x == b.x || a.y == b.y)
  
  def a: Tile
  def b: Tile
  
  lazy val leftBottom = if (a < b) a else b
  lazy val rightUp = if(b < a) a else b
  
  def size = abs(max(a.x - b.x, a.y - b.y))

  def contains(t: Tile): Boolean = (t.x >= leftBottom.x && t.x <= rightUp.x && t.y >= leftBottom.y && t.y <= rightUp.y)
  
}

case class Ship(a: Tile, b: Tile, shots: Seq[Tile] = Seq.empty[Tile]) extends OrthogonalSegment {
  assert(a.x == b.x || a.y == b.y)
  
  def sunk: Boolean = size == shots.size
  def shoot(t: Tile): Ship = {
    if(shots.contains(t) || (!contains(t))) this
    else copy(a,b, shots :+ t)
  }
}