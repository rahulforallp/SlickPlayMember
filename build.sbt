name := """SlickPlayMember"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

sbtVersion := "0.13.7"


libraryDependencies ++= Seq(
  cache,
  ws,
  specs2 % Test,
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P24-B3-SNAPSHOT",
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.slick"  %%     "slick-hikaricp"           %      "3.1.1",
  "ch.qos.logback"       %     "logback-classic"          %      "1.1.3",
  "com.h2database"  % "h2"   %   "1.4.187" ,
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
