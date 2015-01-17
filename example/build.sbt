EclipseKeys.withSource := true

resolvers ++= Seq(
  "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"            at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo"          at "http://repo.spray.io"
)

seq(Revolver.settings: _*)

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000")

libraryDependencies ++= {
  val akkaVersion = "2.3.8"
  val sprayVersion = "1.3.2"
  Seq(
    "io.spray"                %%  "spray-can"       % sprayVersion,
    "io.spray"                %%  "spray-routing"   % sprayVersion,
    "io.spray"                %%  "spray-testkit"   % sprayVersion % "test",
    "io.spray"                %%  "spray-json"      % "1.3.1",  
    "com.typesafe.akka"       %%  "akka-actor"      % akkaVersion,
    "com.typesafe.akka"       %%  "akka-testkit"    % akkaVersion % "test",
    "org.slf4j"               %   "slf4j-api"       % "1.7.10",
    "ch.qos.logback"          %   "logback-core"    % "1.1.2",
    "ch.qos.logback"          %   "logback-classic" % "1.1.2",
    "joda-time"               %   "joda-time"       % "2.7",
    "org.joda"                %   "joda-convert"    % "1.7",
    "org.scalatest"           %%  "scalatest"       % "2.2.1" % "test"
  )
}
