import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val main = Project("main", file(".")) dependsOn(macroSub)
  lazy val macroSub = Project("scala-collection-json-macros", file("macros")) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
}