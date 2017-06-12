enablePlugins(ScalaJSPlugin)

lazy val root = project.in(file("."))
  .aggregate(
    scopegraphJVM,
    scopegraphJS
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val scopegraph = crossProject.in(file("."))
  .settings(
    name := "scopegraph-visualizer",
    version := "1.0",
    scalaVersion := "2.12.2"
  )
  .jvmSettings(

  )
  .jsSettings(
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %%% "scala-parser-combinators" % "1.0.5",
      "org.scalatest" %%% "scalatest" % "3.0.1"
    )
  )

lazy val scopegraphJS = scopegraph.js
lazy val scopegraphJVM = scopegraph.jvm