package battleship

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

@JSExport
object Battleship {
  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {
    val ctx = canvas.getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]
  }
}