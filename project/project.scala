import sbt._
import Keys._

object BuildSettings {
  val buildVersion      = "0.1"
  val buildScalaVersion = "2.11.2"

  val buildSettings = Seq (
    version      := buildVersion,
    scalaVersion := buildScalaVersion
  )
}

object HypersprayBuild extends Build {
  import BuildSettings._

  lazy val core = Project("hyperspray-core", file("core"), settings = buildSettings) dependsOn(macros)
  
  lazy val macros = Project("hyperspray-macros", file("macros"), settings = buildSettings) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  )
  
  lazy val http = Project("hyperspray-http", file("http"), settings = buildSettings) dependsOn(core)

  lazy val example = Project("hyperspray-example", file("example"), settings = buildSettings) dependsOn(http)

  lazy val reactivemongo = Project("hyperspray-reactivemongo", file("reactivemongo"), settings = buildSettings) dependsOn(http)
}
