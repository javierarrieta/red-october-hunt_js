package battleship.model

import java.lang.reflect.InvocationTargetException

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
      Ship(Tile(0, 0), Tile(1, 1)) must throwA[InvocationTargetException]
    }
  }
}
