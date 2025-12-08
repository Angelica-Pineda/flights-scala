import sbt.Keys.libraryDependencies
import sbtassembly.AssemblyPlugin.defaultShellScript

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

ThisBuild / assemblyPrependShellScript := Some(defaultShellScript)

val mainClassName = "org.ntic.flights.FlightsLoaderApp"

lazy val root = (project in file("."))
  .settings(
    name := "tareaScala",

    // clase principal para run y packageBin
    Compile / mainClass := Some("org.ntic.flights.FlightsLoaderApp"),

    // clase principal para assembly
    assembly / mainClass := Some("org.ntic.flights.FlightsLoaderApp"),

    //Nombre del jar
    assembly / assemblyJarName := "flights_loader.jar",

    libraryDependencies ++= Seq(
      //librería de configuración de Typesafe
      "com.typesafe" % "config" % "1.4.3",

      "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.2",
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "org.scala-lang" %% "toolkit-test" % "0.1.7" % Test
    )
  )