import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val colljsonBuilderSub = Project("collectionjson-builder", file("collectionjson-builder")) dependsOn(colljsonMacroSub, colljsonModelSub)
  lazy val colljsonModelSub = Project("collectionjson-model", file("collectionjson-model"))
  lazy val colljsonMacroSub = Project("collectionjson-macros", file("collectionjson-macros")) dependsOn(colljsonModelSub) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
}
