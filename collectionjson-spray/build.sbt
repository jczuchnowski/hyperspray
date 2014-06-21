name := "collectionjson-spray"

version := "0.1"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"            at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo"          at "http://repo.spray.io"
)

EclipseKeys.withSource := true

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000")

libraryDependencies ++= {
  val akkaVersion = "2.3.3"
  val sprayVersion = "1.3.1-20140423"
  Seq(
    "io.spray"                %%  "spray-can"       % sprayVersion,
    "io.spray"                %%  "spray-routing"   % sprayVersion,
    "io.spray"                %%  "spray-testkit"   % sprayVersion % "test",
    "io.spray"                %%  "spray-json"      % "1.2.6",  
    "com.typesafe.akka"       %%  "akka-actor"      % akkaVersion,
    "com.typesafe.akka"       %%  "akka-testkit"    % akkaVersion % "test",
    "org.slf4j"               %   "slf4j-api"       % "1.7.6",
    "ch.qos.logback"          %   "logback-core"    % "1.1.1",
    "ch.qos.logback"          %   "logback-classic" % "1.1.1",
    "joda-time"               %   "joda-time"       % "2.3",
    "org.joda"                %   "joda-convert"    % "1.4",
    "org.scalatest"           %%  "scalatest"       % "2.2.0" % "test"
  )
}
