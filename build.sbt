name := """hadecurt"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.typelevel" %% "scalaz-contrib-210" % "0.1.5" // needed for implicit scalaz.Functor[scala.concurrent.Future]
)
