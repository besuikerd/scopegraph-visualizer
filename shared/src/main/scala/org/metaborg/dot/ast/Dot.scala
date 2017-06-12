package org.metaborg.dot.ast


case class Dot(strict: Boolean, graphType: GraphType, statements: List[DotStatement])






sealed trait DotStatement


case class NodeStatement(id: NodeId, attributes: List[Attribute]) extends DotStatement
case class NodeId(id: String, port: Option[Port])
case class EdgeStatement(from: NodeIdOrSubgraph, to: NodeIdOrSubgraph, attributes: List[Attribute]) extends DotStatement
case class TopLevelAttribute(attribute: Attribute) extends DotStatement
case class AttributeStatement(attributeType: AttributeType, attributes: List[Attribute]) extends DotStatement

sealed trait AttributeType
case class AttributeTypeGraph() extends AttributeType
case class AttributeTypeNode() extends AttributeType
case class AttributeTypeEdge() extends AttributeType

sealed trait Attribute
case class AttributeText(key: String, value: String) extends Attribute
case class AttributeHtml(key: String, value: HtmlValue) extends Attribute  //TODO proper html format

sealed trait HtmlValue
case class HtmlValueLiteral(lit: String) extends HtmlValue

case class SubGraph(name: Option[String], statements: List[DotStatement]) extends DotStatement

sealed trait NodeIdOrSubgraph
object NodeIdOrSubgraph{
  implicit def caseNodeId(nodeId: NodeId): CaseNodeId = CaseNodeId(nodeId)
  implicit def caseSubgraph(subGraph: SubGraph): CaseSubgraph = CaseSubgraph(subGraph)
}
case class CaseNodeId(nodeId: NodeId) extends NodeIdOrSubgraph
case class CaseSubgraph(subGraph: SubGraph) extends NodeIdOrSubgraph

sealed trait Port
case class PortNamed(id: String, compassPoint: Option[CompassPoint]) extends Port
case class PortCompass(compassPoint: CompassPoint) extends Port


sealed trait CompassPoint
case class N() extends CompassPoint
case class NE() extends CompassPoint
case class E() extends CompassPoint
case class SE() extends CompassPoint
case class S() extends CompassPoint
case class SW() extends CompassPoint
case class W() extends CompassPoint
case class NW() extends CompassPoint
case class C() extends CompassPoint
case class $_() extends CompassPoint

sealed trait GraphType
case class TypeGraph() extends GraphType
case class TypeDiGraph() extends GraphType


trait DotDsl{
  import NodeIdOrSubgraph._

  def digraph(stmts: DotStatement*): Dot = Dot(false, TypeDiGraph(), stmts.toList)

  def nodeId(name: String, portId: String): NodeId = NodeId(name, Some(PortNamed(portId, None)))
  def nodeId(name: String, portId: String, compassPoint: CompassPoint): NodeId = NodeId(name, Some(PortNamed(portId, Some(compassPoint))))
  def nodeId(name: String, port: Option[Port] = None): NodeId = NodeId(name, port)
  def node(name: String, attributes: Attribute*): NodeStatement = node(NodeId(name, None), attributes:_*)
  def node(nodeId: NodeId, attributes: Attribute*): NodeStatement = NodeStatement(nodeId, attributes.toList)
  def edge(from: String, to: String, attributes: Attribute*): EdgeStatement = EdgeStatement(nodeId(from), nodeId(to), attributes.toList)
  def edge(from: NodeId, to: NodeId, attributes: Attribute*): EdgeStatement = EdgeStatement(from, to, attributes.toList)

  def attribute(attribute: Attribute): TopLevelAttribute = TopLevelAttribute(attribute)
  def graphAttributes(attributes: Attribute*) = AttributeStatement(AttributeTypeGraph(), attributes.toList)
  def nodeAttributes(attributes: Attribute*) = AttributeStatement(AttributeTypeNode(), attributes.toList)
  def edgeAttributes(attributes: Attribute*) = AttributeStatement(AttributeTypeEdge(), attributes.toList)

  object compass{
    val n = N()
    val ne = NE()
    val e = E()
    val se = SE()
    val s = S()
    val sw = SW()
    val w = W()
    val nw = NW()
    val c = C()
    val wildcard = $_
  }

  def subgraph(name: String, stmts: DotStatement*): SubGraph = SubGraph(Some(name), stmts.toList)
  def subgraph(stmts: DotStatement*): SubGraph = SubGraph(None, stmts.toList)

  def html(lit: String): HtmlValue = HtmlValueLiteral(lit)

  def shape(value: String): Attribute = 'shape -> value
  val shapeRecord: Attribute = shape("record")

  implicit def kvToHtmlAttribute(tpl: (Symbol, HtmlValue)): Attribute = AttributeHtml(tpl._1.name, tpl._2)
  implicit def kvToTextAttribute(tpl: (Symbol, String)): Attribute = AttributeText(tpl._1.name, tpl._2)
  implicit def kvToEdge(tpl: (String, String)): EdgeStatement = edge(tpl._1, tpl._2)
}