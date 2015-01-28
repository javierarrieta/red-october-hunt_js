package battleship.model

import scala.annotation.tailrec
import scala.util.Random

case class Tile(x: Int, y: Int) {
  def < (t: Tile): Boolean = x < t.x || y < t.y
  def > (t: Tile): Boolean = x > t.x || y > t.y
  def - (xOff: Int, yOff: Int) = Tile(x - xOff, y - yOff)
  def + (xOff: Int, yOff: Int) = Tile(x + xOff, y + yOff)
}

trait OrthogonalSegment {
  
  protected def validate() = assert(a.x == b.x || a.y == b.y)
  
  def a: Tile
  def b: Tile
  
  lazy val leftBottom = if (a < b) a else b
  lazy val rightTop = if(b < a) a else b
  
  lazy val size = rightTop.x - leftBottom.x + rightTop.y - leftBottom.y + 1

  def contains(t: Tile): Boolean = (t.x >= leftBottom.x && t.x <= rightTop.x && t.y >= leftBottom.y && t.y <= rightTop.y)
  def containsOrAdjacent(t: Tile): Boolean = (t.x >= leftBottom.x - 1 && t.x <= rightTop.x + 1
                                           && t.y >= leftBottom.y - 1 && t.y <= rightTop.y + 1)
  
  lazy val tiles: Seq[Tile] = {
    if(leftBottom.x == rightTop.x) Range(leftBottom.y, rightTop.y + 1).map(Tile(leftBottom.x, _))
    else Range(leftBottom.x, rightTop.x + 1).map(Tile(_,leftBottom.y))
  }
}

case class Ship(a: Tile, b: Tile, shots: Seq[Tile] = Seq.empty[Tile]) extends OrthogonalSegment {
  def overlaps(ship: Ship): Boolean = tiles.foldLeft(false)(_ || ship.contains(_))
  def overlapsOrAdjacent(ship: Ship): Boolean = tiles.foldLeft(false)(_ || ship.containsOrAdjacent(_))

  validate()
  
  def remaining = size - shots.size
  def sunk: Boolean = remaining == 0
  def shoot(t: Tile): Ship = {
    if(shots.contains(t) || (!contains(t))) this
    else copy(a,b, shots :+ t)
  }
}

case class Board(size: Int, ships: Seq[Ship]) {
  lazy val tilesRemaining = ships.foldLeft(0)(_ + _.remaining)
  lazy val shipsRemaining = ships.filter(!_.sunk)
  lazy val shotsInTarget = ships.flatMap(_.shots)
  lazy val shipTiles = ships.flatMap(_.tiles)
  lazy val lost = tilesRemaining == 0
  def overlaps(ship: Ship): Boolean = ships.foldLeft(false)(_ || _.overlapsOrAdjacent(ship))
  
  def tileType(t: Tile): TileType = {
    if (shotsInTarget.contains(t)) Wreck
    else if(shipTiles.contains(t)) Deck
    else Water
  }
}

object Board {
  def random(size: Int, maxShipSize: Int): Board = {
    @tailrec
    def generateShips(acc: Seq[Int])(number: Int, remaining: Int): Seq[Int] = remaining match {
      case 1 => acc //2 tiles is the smallest size of boat
      case _ => generateShips(acc ++ Seq.fill(number)(remaining))(number + 1, remaining - 1)
    }
    def allocateShip(board: Board, shipSize: Int): Board = {
      val ship = randomShip(board.size, shipSize)
      if(board.overlaps(ship))
        allocateShip(board, shipSize)
      else
        board.copy(ships = board.ships :+ ship)
    }
    generateShips(Nil)(1,maxShipSize).foldLeft(Board(size, Nil))(allocateShip)
  }
  
  private def randomShip(boardSize: Int, shipSize: Int): Ship = {
    val horizontal = Random.nextBoolean()
    val orig = Random.nextInt(boardSize - shipSize + 1)
    val otherCoord = Random.nextInt(boardSize)
    Ship(
      Tile(
        if(horizontal) orig else otherCoord,
        if(horizontal) otherCoord else orig
      ),
      Tile(
        if(horizontal) orig + shipSize - 1 else otherCoord,
        if(horizontal) otherCoord else orig + shipSize - 1
      )
    )
  }
  
}

case class Player(name: String, board: Board)

case class Game(playerA: Player, playerB: Player) {
  assert(playerA.board.size == playerB.board.size)
}

sealed trait TileType
object Water extends TileType
object Deck  extends TileType
object Wreck extends TileType

case class OnlinePlayer(username: String)
