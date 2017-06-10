
name := "scopegraph-visualizer"

version := "1.0"

scalaVersion := "2.12.2"

enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scala-lang.modules" %%% "scala-parser-combinators" % "1.0.5"
scalaJSModuleKind := ModuleKind.CommonJSModule
//jsDependencies += "org.webjars" % "viz.js" % "0.0.3" / "0.0.3/viz.js"