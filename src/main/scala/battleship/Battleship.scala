package battleship

import battleship.model._
import org.scalajs.dom.{HTMLElement, document}
import scala.scalajs.js.Dynamic.{ global => g }
import battleship.firebase.FirebaseMe
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport
import scala.util.Random

@JSExport
object Battleship {

  val names = List("Javier", "Greg", "Juan", "Devin", "Jonathan", "Manoj")
  val myBoard = Board.random(10, 5)
  val player = Player(Random.shuffle(names).head, myBoard)
  
  @JSExport
  def fire(row: Int, col: Int): Unit = {
     g.console.log(s"""firing  row  $row, column $col""")
  }

  @JSExport
  def main(myBoardDiv: HTMLElement): Unit = {
    paintBoard(myBoardDiv)(myBoard)
  }
  
  @JSExport
  def paintBoard(div: HTMLElement)(board: Board): Unit = {
    val waterStyle = "background-color: blue; float: left; width: 20px; height: 20px; margin: 2px; display: block; vertical-align: top; overflow:visible"
    val deckStyle = "background-color: black; float: left; width: 20px; height: 20px; margin: 2px; display: block; vertical-align: top; overflow:visible"
    val wreckStyle = "background-color: red; float: left; width: 20px; height: 20px; margin: 2px; display: block; vertical-align: top; overflow:visible"
    
    def tileMapping(tyleType: TileType): String = tyleType match {
      case Water => waterStyle
      case Wreck => wreckStyle
      case Deck  => deckStyle
    }
    
    for (i <- Range(0,board.size)) {
      for (j <- Range(0,board.size)) {
        val tile = Tile(j,i)
        val cell = document.createElement("div")
        cell.setAttribute("row", s"""$i""")
        cell.setAttribute("col", s"""$j""")
        cell.setAttribute("onclick", s"""Battleship().fire($i, $j)""")
        cell.setAttribute("style", tileMapping(board.tileType(tile)))
        cell.setAttribute("id", s"$i-$j")
        div.appendChild(cell)
      }
      val clear = document.createElement("div")
      clear.setAttribute("style", "clear: both")
      div.appendChild(clear)
    }
    
  }
}