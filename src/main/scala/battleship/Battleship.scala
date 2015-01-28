package battleship

import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, document}
import scala.scalajs.js.Dynamic.{ global => g }

import scala.scalajs.js.annotation.JSExport





@JSExport
object Battleship {
  @JSExport
  def fire(row: Int, col: Int): Unit = {
     g.console.log(s"""firing  row  $row, column $col""")
  }


  @JSExport
  def main(): Unit = {

    val side =9
    val water = "blue"
    val miss = "grey"
    val hit = "orange"
    val sunk ="black"

    val boards: HTMLElement = document.createElement("div")
    boards.setAttribute("id","boards")
    //boards.setAttribute("style", "width: 1500; height: 750;")
    document.body.appendChild(boards)
    val myboard: HTMLElement = document.createElement("div")
    myboard.setAttribute("id","myboard")

    //render title
    val mytitle = document.createElement("h2")
    val text = document.createTextNode("My board")
    mytitle.appendChild(text)
    boards.appendChild(mytitle)


    //render my own board
    for (i <- Range(0,8)){
      for (j <- Range(0,8)) {
       val cell = document.createElement("div")
        cell.setAttribute("row", s"""$i""")
        cell.setAttribute("col", s"""$j""")
        cell.setAttribute("style", "background-color: blue; float: left; width: 20px; height: 20px; margin: 2px; display: block; vertical-align: top; overflow:visible")
        myboard.appendChild(cell)
      }
      val clear = document.createElement("div")
      clear.setAttribute("style", "clear: both")
      myboard.appendChild(clear)
    }
    boards.appendChild(myboard)
    
    //render title for oponent's board
    val oponenttitle = document.createElement("h2")
    val text2 = document.createTextNode("Oponent board")
    oponenttitle.appendChild(text2)
    boards.appendChild(oponenttitle)

    //render oponent board
    val oponentsBoard = document.createElement("div")
    oponentsBoard.setAttribute("id","opponent")
    for (i <- Range(0,8)){
      for (j <- Range(0,8)) {
        val cell = document.createElement("div")
        cell.setAttribute("row", s"""$i""")
        cell.setAttribute("col", s"""$j""")
        cell.setAttribute("onclick", s"""Battleship().fire($i, $j)""")
        cell.setAttribute("style", "background-color: blue; float: left; width: 20px; height: 20px; margin: 2px; display: block; vertical-align: top; overflow:visible")
        oponentsBoard.appendChild(cell)
      }
      val clear = document.createElement("div")
      clear.setAttribute("style", "clear: both")
      oponentsBoard.appendChild(clear)
    }


    boards.appendChild(oponentsBoard)
  }
}