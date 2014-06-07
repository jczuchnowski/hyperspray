import AssemblyKeys._

name := "scala-collection-json"

version := "1.0.0"

scalaVersion := "2.10.4"

resolvers ++= Seq(
  "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"            at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

EclipseKeys.withSource := true

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000")

libraryDependencies ++= {
  Seq(
    "org.slf4j"               %   "slf4j-api"       % "1.7.6",
    "ch.qos.logback"          %   "logback-core"    % "1.1.1",
    "ch.qos.logback"          %   "logback-classic" % "1.1.1",
    "joda-time"               %   "joda-time"       % "2.3",
    "org.joda"                %   "joda-convert"    % "1.4",
    "org.scalaz"              %%  "scalaz-core"     % "7.0.5",
    "org.specs2"              %%  "specs2"          % "2.3.11" % "test",
    "org.mockito"             %   "mockito-all"     % "1.9.5" % "test",
	  "junit"                   %   "junit"           % "4.11" % "test"
  )
}
