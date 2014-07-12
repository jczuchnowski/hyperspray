import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val core = Project("hyperspray-core", file("core")) dependsOn(macros)
  
  lazy val macros = Project("hyperspray-macros", file("macros")) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
  
  lazy val http = Project("hyperspray-http", file("http")) dependsOn(core)

  lazy val example = Project("hyperspray-example", file("example")) dependsOn(http)
}
