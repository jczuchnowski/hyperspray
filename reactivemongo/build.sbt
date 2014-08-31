resolvers ++= Seq(
  "snapshots"           at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases"            at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= {
  Seq(
    "org.reactivemongo"       %% "reactivemongo"    % "0.10.5.akka23-SNAPSHOT"
  )
}