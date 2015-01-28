package battleship.firebase

import battleship.model.{OnlinePlayer, Game, Player}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

import upickle._

/**
 * 
 */
@JSExport
object FirebaseMe {

  lazy val fb = js.Dynamic.global.fb
  lazy val fbPlayers = fb.child("players")
  lazy val fbGames = fb.child("games")
  lazy val fbScoreboard = fb.child("scoreboard")

  @JSExport
  def invitePlayer(id: String) = {

    println("HEY BRO!!! " + fb)
  }

  def signIn(name: String, newGame: Game => Unit) = {
    println(s"Signing in $name")
    // Generate a reference to a new location and add some data using push()
    val json = js.Dynamic.literal(name -> true)
    println(s"Pushing ${json.toString}")
    fbPlayers.set(json)

    val onNewGame: (String => Unit) = { jsonString =>
      newGame(read[Game](jsonString))
    }

    fb.child(s"invites/$name").on("child_added", onNewGame)
  }

  def signOut(username: String) = {
    println(s"Signing out $username")
    fbPlayers.set(js.Dynamic.literal(username -> false))
  }

  def makeMove(gameId: String, player: String, row: String, column: String) = {
    val ref = fb.child(s"moves/$gameId").push(js.Dynamic.literal("player" -> player, "row" -> row, "column" -> column))
    println(s"Made move for gameId $gameId, player $player, row $row, col $column: ${ref.key()}")
  }

  def invite(game: Game): String = {
    val player1 = game.playerA.name
    val player2 = game.playerB.name
    val sorted = List(player1, player2).sorted
    val gameId = s"${sorted.head}|${sorted.last}|${System.currentTimeMillis()}"
    val gameJson = write(game)
    val gameRef = fb.child(s"invites/$player1").push(js.Dynamic.literal("player" -> player2, "gameId" -> gameId, "board" -> gameJson))
    gameRef.key().toString
  }
}
