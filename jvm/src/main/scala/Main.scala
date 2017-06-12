import org.metaborg.dot.pp.DotPrettyPrinter
import org.metaborg.pp.DocPrinter
import org.metaborg.scopegraph.generate.dot.ScopeGraphToDot
import org.metaborg.scopegraph.parser.ScopeGraphParser

import scala.io.Source

object Main extends App{
  val input = Source.fromResource("test.scopegraph", getClass.getClassLoader).mkString

  val transform =
    ScopeGraphToDot.scopeGraphToDot _ andThen DotPrettyPrinter.ppDot _ andThen DocPrinter.pp _
  ScopeGraphParser.parse(input).fold(
    println,
    transform andThen println
  )
}