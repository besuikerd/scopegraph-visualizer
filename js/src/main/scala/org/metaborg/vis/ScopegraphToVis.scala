package org.metaborg.vis

import org.metaborg.json.ast.{JSON, JSONObject}
import org.metaborg.scopegraph.ast.{DirectEdge, ImportEdge, _}
import org.metaborg.scopegraph.pp.ScopeGraphPrettyPrinter
import org.metaborg.vis.VisNode.NodeId

sealed trait NodeType
case class ScopeNodeType(scopeName: ScopeName) extends NodeType()
case class RefNodeType(occurrence: Occurrence) extends NodeType()
case class DeclNodeType(occurrence: Occurrence) extends NodeType()


sealed trait EdgeType
case class DeclEdgeType() extends EdgeType
case class RefEdgeType() extends EdgeType
case class DirectEdgeType(label: String) extends EdgeType
case class ImportEdgeType(label: String) extends EdgeType
case class AssocEdgeType(label: String) extends EdgeType

case class VisNode(
  id: NodeId,
  label: String,
  nodeType: NodeType
)
object VisNode{
  type NodeId = Int
}

case class VisEdge(
  from: NodeId,
  to: NodeId,
  edgeType: EdgeType
)
case class VisDataset(nodes: List[VisNode], edges: List[VisEdge])


object ScopegraphToVis {
  import JSON.Implicits._
  import JSON._
  import org.metaborg.pp.Doc.Implicits.RichDoc

  def scopegraphToVis(scopegraph: ScopeGraph): JSON =
    visDataSetToJSON(
      scopegraphToVisDataset(scopegraph)
    )

  def scopegraphToVisDataset(scopeGraph: ScopeGraph): VisDataset = {
    val dataset = VisDataset(List(), List())
    val scopeNodes = scopeGraph.scopeEntries.zipWithIndex.map(Function.tupled(processScopeEntryScopeName))
    val dataset2 = scopeNodes.map(_._1).foldLeft(dataset){case (dataset, node) => dataset.copy(nodes = node :: dataset.nodes)}
    scopeNodes.foldLeft(dataset2){
      case (dataset, (node, scopePartEntries)) => {
        implicit val scopeNode = node
        scopePartEntries.foldLeft(dataset)(processScopePartEntry)
      }
    }
  }

  def processScopeEntryScopeName(scopeEntry: ScopeEntry, nodeId: NodeId): (VisNode, List[ScopePartEntry])= scopeEntry match {
    case Scope(scopeName, scopePartEntries) =>
      val name = ScopeGraphPrettyPrinter.ppScopeName(scopeName).pp
      val node = VisNode(nodeId, name, ScopeNodeType(scopeName))
      (node, scopePartEntries)
  }

  def processScopePartEntry(visDataset: VisDataset, scopePartEntry: ScopePartEntry)(implicit scopeNode: VisNode): VisDataset = scopePartEntry match {
    case Decls(declEntries) => declEntries.foldLeft(visDataset)(processDeclEntry)
    case Refs(refEntries) => refEntries.foldLeft(visDataset)(processRefEntry)
    case DirectEdges(directEdges) => directEdges.foldLeft(visDataset)(processDirectEntry)
    case ImportEdges(importEdges) => importEdges.foldLeft(visDataset)(processImportEntry)
    case AssocEdges(assocEdges) => assocEdges.foldLeft(visDataset)(processAssocEntry)
  }

  def processDeclEntry(visDataset: VisDataset, declEntry: DeclEntry)(implicit scopeNode: VisNode): VisDataset = declEntry match {
    case Decl(occurrence, declType) =>
      val node = VisNode(visDataset.nodes.length + 1, ScopeGraphPrettyPrinter.ppOccurrence(occurrence).pp, DeclNodeType(occurrence))
      val edge = VisEdge(scopeNode.id, node.id, DeclEdgeType())
      visDataset.copy(nodes = node :: visDataset.nodes, edges = edge :: visDataset.edges)
  }

  def processRefEntry(visDataset: VisDataset, refEntry: RefEntry)(implicit scopeNode: VisNode): VisDataset = refEntry match {
    case Ref(occurrence) =>
      val node = VisNode(visDataset.nodes.length + 1, ScopeGraphPrettyPrinter.ppOccurrence(occurrence).pp, RefNodeType(occurrence))
      val edge = VisEdge(node.id, scopeNode.id, RefEdgeType())
      visDataset.copy(nodes = node :: visDataset.nodes, edges = edge :: visDataset.edges)
  }

  def processDirectEntry(visDataset: VisDataset, directEntry: DirectEntry)(implicit scopeNode: VisNode): VisDataset = directEntry match {
    case DirectEdge(pathLabel, scopeName) =>
      val nodeIdTo = visDataset.nodes.collectFirst{
        case VisNode(id, _, ScopeNodeType(scopeName2)) if scopeName == scopeName2 => id
      }.getOrElse(-1)
      val edge = VisEdge(scopeNode.id, nodeIdTo, DirectEdgeType(pathLabel))
      visDataset.copy(edges = edge :: visDataset.edges)
  }

  def processImportEntry(visDataset: VisDataset, importEntry: ImportEntry)(implicit scopeNode: VisNode): VisDataset = importEntry match {
    case ImportEdge(pathLabel, occurrence) =>
      val nodeIdTo = visDataset.nodes.collectFirst{
        case VisNode(id, _, DeclNodeType(occurrence)) => id
        case VisNode(id, _, RefNodeType(occurrence)) => id
      }.getOrElse(-1)
      val edge = VisEdge(scopeNode.id, nodeIdTo, ImportEdgeType(pathLabel))
      visDataset.copy(edges = edge :: visDataset.edges)
  }

  def processAssocEntry(visDataset: VisDataset, assocEntry: AssocEntry)(implicit scopeNode: VisNode): VisDataset = assocEntry match {
    case AssocEdge(pathLabel, occurrence) =>
      val optNodeIdTo = occurrence match {
        case OccurrenceAt(namespace, cTerm, OccurrenceIndexFrom(scopeName))=>
          visDataset.nodes.collectFirst{
            case VisNode(id, _, ScopeNodeType(scopeName2)) if scopeName == scopeName2 => id
          }
        case _ => None
      }
      val edge = VisEdge(scopeNode.id, optNodeIdTo.getOrElse(-1), ImportEdgeType(pathLabel))
      visDataset.copy(edges = edge :: visDataset.edges)
  }

  def visDataSetToJSON(visDataset: VisDataset): JSON = {
    obj(
      "nodes" -> visDataset.nodes.map(visNodeToJSON),
      "edges" -> visDataset.edges.map(visEdgeToJSON)
    )
  }

  def visNodeToJSON(node: VisNode): JSON = obj(
    "id" -> node.id,
    "label" -> node.label,
    "group" -> visNodeTypeToJSON(node.nodeType)
  )

  def visNodeTypeToJSON(nodeType: NodeType): JSON = nodeType match {
    case ScopeNodeType(scopeName) => "scope"
    case RefNodeType(occurrence) => "ref"
    case DeclNodeType(occurrence) => "decl"
  }

  def visEdgeToJSON(edge: VisEdge): JSON = obj(
    "from" -> edge.from,
    "to" -> edge.to
  ).merge(visEdgeTypeOptions(edge.edgeType))

  def visEdgeTypeOptions(edgeType: EdgeType): JSONObject = edgeType match {
    case DeclEdgeType() => obj(
      "arrows" -> obj(
        "to" -> obj(
          "enabled" -> true
        )
      )
    )
    case RefEdgeType() => obj(
      "arrows" -> obj(
        "to" -> obj(
          "enabled" -> true
        )
      )
    )
    case DirectEdgeType(label) => obj(
      "arrows" -> obj(
        "to" -> obj(
          "enabled" -> true
        )
      ),
      "label" -> label
    )
    case ImportEdgeType(label) => obj(
      "arrows" -> obj(
        "to" -> obj(
          "enabled" -> true,
          "type" -> "circle"
        )
      ),
      "label" -> label
    )
    case AssocEdgeType(label) => obj(
      "arrows" -> obj(
        "to" -> obj(
          "enabled" -> true,
          "type" -> "circle"
        )
      ),
      "label" -> label
    )
  }
}
