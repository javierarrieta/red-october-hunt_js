package battleship.model

import org.specs2.mutable.Specification

class ModelSpec extends Specification {

  "A ship" should {
    val ship = Ship(Tile(0,0), Tile(0,1))
    "contain its ends" in {
      ship.contains(Tile(0,0)) must beTrue
      ship.contains(Tile(0,1)) must beTrue
    }
    "not contain outer tiles" in {
      ship.contains(Tile(1,0)) must beFalse
      ship.contains(Tile(1,1)) must beFalse
    }
    "overlap other crossing ship" in {
      val overlapping = Ship(Tile(0,0), Tile(1,0))
      overlapping.overlaps(ship) must beTrue
      ship.overlaps(overlapping) must beTrue
    }
    "not allow to create a non orthogonal ship" in {
      Ship(Tile(0, 0), Tile(1, 1)) must throwA[AssertionError]
    }
    "have a size corresponding to the number of tiles" in {
      ship.size must equalTo(2)
    }
  }
  
  "A board" should {

    val ships = Seq(Ship(Tile(0, 0), Tile(0, 1)), Ship(Tile(2, 2), Tile(2, 3)))
    val board = Board(4, ships)
    "report all ships as remaining if none sunk" in {
      board.shipsRemaining must equalTo(ships)
      board.tilesRemaining must equalTo(4)
    }
    
    val generated = Board(10, 5)
    
    "generated should have the correct number of ships" in {
      generated.shipsRemaining.size must equalTo(15)
      generated.tilesRemaining must equalTo(35)
    }
    
    "generated should have the correct number of tiles per type" in {
      val tiles = for {
        i <- Range(0, generated.size)
        j <- Range(0, generated.size)
      } yield Tile(j,i)
      
      val types = tiles.map(generated.tileType)

      val boardTiles = generated.size * generated.size
      types.size must equalTo(boardTiles)
      types.count(_ == Wreck) must equalTo(0)
      types.count(_ == Deck)  must equalTo(generated.tilesRemaining)
      types.count(_ == Water) must equalTo(boardTiles - generated.tilesRemaining)
    }
  }
}
