import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val main = Project("main", file(".")) dependsOn(macroSub, modelSub)
  lazy val modelSub = Project("scala-collection-json-model", file("model"))
  lazy val macroSub = Project("scala-collection-json-macros", file("macros")) dependsOn(modelSub) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
}