package org.metaborg.dot.pp

import org.metaborg.dot.ast._
import org.metaborg.pp.Doc
import org.metaborg.pp.Doc.empty

object DotPrettyPrinter {
  import Doc._
  import Doc.Implicits._
  import org.metaborg.util.Implicits._

  def ppDot(dot: Dot): Doc = {
    implicit val implicitDot = dot
    ppGraphType(dot.graphType) <> "{" $$
      nest(2, sep(dot.statements.map(ppDotStatement))) $$
      "}"

  }

  def ppDotStatement(dotStatement: DotStatement)(implicit dot: Dot): Doc = dotStatement match {
    case nodeStatement: NodeStatement => ppNodeStatement(nodeStatement)
    case edgeStatement: EdgeStatement => ppEdgeStatement(edgeStatement)
    case subgraph: SubGraph => ppSubGraph(subgraph)
    case TopLevelAttribute(attribute) => ppAttribute(attribute)
    case attributeStatement: AttributeStatement => ppAttributeStatement(attributeStatement)
  }

  def ppNodeStatement(nodeStatement: NodeStatement): Doc = {
    val NodeStatement(id, attributes) = nodeStatement
    ppNodeId(id) <> ppAttributes(attributes)
  }

  def ppEdgeStatement(edgeStatement: EdgeStatement)(implicit dot: Dot): Doc ={
    val EdgeStatement(from, to, attributes) = edgeStatement
    ppNodeIdOrSubGraph(from) <> ppEdge <> ppNodeIdOrSubGraph(to) <> ppAttributes(attributes)
  }

  def ppAttributeStatement(attributeStatement: AttributeStatement): Doc = {
    val AttributeStatement(attributeType, attributes) = attributeStatement
    ppAttributeType(attributeType) <> ppAttributes(attributes)
  }

  def ppAttributeType(attributeType: AttributeType): Doc = attributeType match{
    case AttributeTypeGraph() => "graph"
    case AttributeTypeNode() => "node"
    case AttributeTypeEdge() => "edge"
  }


  def ppNodeIdOrSubGraph(nodeIdOrSubgraph: NodeIdOrSubgraph)(implicit dot: Dot) = nodeIdOrSubgraph match {
    case CaseNodeId(nodeId) => ppNodeId(nodeId)
    case CaseSubgraph(subGraph) => ppSubGraph(subGraph)
  }

  def ppSubGraph(subGraph: SubGraph)(implicit dot: Dot): Doc = {
    val name: Doc = subGraph.name match {
      case Some(name) => " " <> name
      case None => empty
    }
    "subgraph" <> name <> " {" $$
      nest(2, sep(subGraph.statements.map(ppDotStatement))) $$
      "}"
  }

  def ppAttributes(attributes: List[Attribute]): Doc =
    if(attributes.isEmpty)
      empty
    else
      "[" <> sep(attributes.map(ppAttribute)) <> "]"

  def ppAttribute(attribute: Attribute): Doc = attribute match {
    case AttributeText(key, value) => key <> "=\"" <> value <> "\""
    case AttributeHtml(key, value) => key <> "=" <> ppHtmlValue(value)
  }

  def ppHtmlValue(htmlValue: HtmlValue): Doc = htmlValue match {
    case HtmlValueLiteral(lit) => lit
  }

  def ppNodeId(nodeId: NodeId): Doc = "\"" <> nodeId.id.unquote <> "\"" <> nodeId.port.map(ppPort).getOrElse(empty)

  def ppPort(port: Port): Doc = port match {
    case PortCompass(compassPoint) => ":" <> ppCompassPoint(compassPoint)
    case PortNamed(id, compassPoint) => ":" <> id <> (compassPoint match{
      case Some(compassPoint) => ":" <> ppCompassPoint(compassPoint)
      case None => empty
    })
  }

  def ppCompassPoint(compassPoint: CompassPoint): Doc = compassPoint match {
    case N() => "n"
    case NE() => "ne"
    case E() => "e"
    case SE() => "se"
    case S() => "s"
    case SW() => "sw"
    case W() => "w"
    case NW() => "nw"
    case C() => "c"
    case $_() => "_"
  }

  def ppEdge(implicit dot: Dot): Doc = dot.graphType match {
    case TypeGraph() => "--"
    case TypeDiGraph() => "->"
  }


  def ppGraphType(graphType: GraphType): Doc = graphType match {
    case TypeGraph() => "graph"
    case TypeDiGraph() => "digraph"
  }
}
