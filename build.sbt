import scala.scalajs.sbtplugin.ScalaJSPlugin._
import com.lihaoyi.workbench.Plugin._

scalaJSSettings

workbenchSettings

name := "Battleship Game"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
  "org.webjars" % "firebase" % "2.0.4",
  "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

bootSnippet := "Battleship().main(document.getElementById('canvas'));"

updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.fastOptJS in Compile)

