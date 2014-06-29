import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val colljsonBuilder = Project("collectionjson-builder", file("collectionjson-builder")) dependsOn(colljsonMacro, colljsonModel)
  
  lazy val colljsonModel = Project("collectionjson-model", file("collectionjson-model"))
  
  lazy val colljsonMacro = Project("collectionjson-macros", file("collectionjson-macros")) dependsOn(colljsonModel) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
  
  lazy val colljsonSpray = Project("collectionjson-spray", file("collectionjson-spray")) dependsOn(colljsonBuilder, colljsonModel)

  lazy val example = Project("hyperspray-example", file("example")) dependsOn(colljsonSpray)
}
