package main.scala

import org.metaborg.dot.ast.Dot
import org.metaborg.dot.pp.DotPrettyPrinter
import org.metaborg.json.ast.JSON
import org.metaborg.json.pp.JSONPrettyPrinter
import org.metaborg.pp.DocPrinter
import org.metaborg.scopegraph.ast.ScopeGraph
import org.metaborg.scopegraph.generate.dot.ScopeGraphToDot
import org.metaborg.scopegraph.generate.json.ScopegraphToJSON
import org.metaborg.scopegraph.parser.ScopeGraphParser

import scala.scalajs.js.annotation.JSExportTopLevel

object JavascriptExports {
  @JSExportTopLevel("parseScopegraph")
  def parseScopegraph(input: String): Any = {
    val result = ScopeGraphParser.parse(input).fold[Any](
      error => error,
      scopegraph => scopegraph
    )
    result
  }

  @JSExportTopLevel("scopegraphToDot")
  def scopegraphToDot(scopegraph: ScopeGraph): Dot = ScopeGraphToDot.scopeGraphToDot(scopegraph)

  @JSExportTopLevel("ppDot")
  def ppDot(dot: Dot): String = DocPrinter.pp(DotPrettyPrinter.ppDot(dot))

  @JSExportTopLevel("ppJSON")
  def ppJSON(json: JSON): String = DocPrinter.pp(JSONPrettyPrinter.ppJSON(json))

  @JSExportTopLevel("scopegraphToJSON")
  def scopegraphToJSON(scopeGraph: ScopeGraph): JSON = ScopegraphToJSON.scopegraphToJSON(scopeGraph)
}
