package org.metaborg.scopegraph.generate.dot

import org.metaborg.dot.ast._
import org.metaborg.pp.DocPrinter
import org.metaborg.scopegraph.ast._

object ScopeGraphToDot {
  import RichDotDsl._
  import org.metaborg.pp.Doc.Implicits._
  import org.metaborg.scopegraph.pp.CTermPrettyPrinter._
  import org.metaborg.scopegraph.pp.ScopeGraphPrettyPrinter._
  import org.metaborg.util.Implicits._


  def scopeGraphToDot(scopeGraph: ScopeGraph): Dot = {
    val statements =
      List.concat(
        scopeGraphOptions,
        scopeGraph.scopeEntries.flatMap(scopeEntryToDotStatements)
      )
    digraph(statements:_*)
  }

  def scopeGraphOptions: List[DotStatement] = List(
    attribute('rankdir -> "BT")
  )

  def scopeEntryToDotStatements(scopeEntry: ScopeEntry): List[DotStatement] = scopeEntry match {
    case Scope(name, scopePartEntries) => {
      scopeNameToNode(name) :: scopePartEntries.flatMap(scopePartEntryToDotStatements(name))
    }
  }

  def scopePartEntryToDotStatements(scope: ScopeName)(scopePartEntry: ScopePartEntry): List[DotStatement] = scopePartEntry match {
    case Decls(declEntries) => declEntries.flatMap(declEntryToDotStatements(scope))
    case Refs(refEntries) => refEntries.flatMap(refEntryToDotStatements(scope))
    case DirectEdges(directEdges) => directEdges.flatMap(directEntryToDotStatements(scope))
    case ImportEdges(importEdges) => importEdges.flatMap(importEdgeToDotStatements(scope))
    case AssocEdges(assocEdges) => assocEdges.flatMap(assocEdgeToDotStatements(scope))
  }

  def declEntryToDotStatements(scope: ScopeName)(decl: DeclEntry): List[DotStatement] = decl match {
    case Decl(occurrence, declType) =>
      List(
        occurrenceToNodeStatement(occurrence),
        edge(scopeNameToNodeId(scope), occurrenceToNodeId(occurrence))
      )
  }

  def refEntryToDotStatements(scope: ScopeName)(ref: RefEntry): List[DotStatement] = ref match {
    case Ref(occurrence) =>
      List(
        occurrenceToNodeStatement(occurrence),
        edge(occurrenceToNodeId(occurrence), scopeNameToNodeId(scope))
      )
  }

  def directEntryToDotStatements(scope: ScopeName)(directEntry: DirectEntry): List[DotStatement] = directEntry match {
    case DirectEdge(pathLabel, toScope) =>
      List(
        node(scopeNameToNodeId(toScope)),
        edge(scopeNameToNodeId(scope), scopeNameToNodeId(toScope), 'label -> pathLabel)
      )
  }

  def importEdgeToDotStatements(scope: ScopeName)(importEntry: ImportEntry): List[DotStatement] = importEntry match {
    case ImportEdge(pathLabel, occurrence) =>
      List(
        occurrenceToNodeStatement(occurrence),
        edge(
          scopeNameToNodeId(scope),
          occurrenceToNodeId(occurrence),
          'label -> pathLabel,
          'arrowhead -> "empty"
        )
      )
  }

  def assocEdgeToDotStatements(scope: ScopeName)(assocEntry: AssocEntry): List[DotStatement] = assocEntry match {
    case AssocEdge(pathLabel, occurrence) =>
      List(
        occurrenceToNodeStatement(occurrence),
        edge(
          occurrenceToNodeId(occurrence),
          scopeNameToNodeId(scope),
          'label -> pathLabel,
          'arrowhead -> "empty"
        )
      )
  }

  def occurrenceToNodeStatement(occurrence: Occurrence): NodeStatement = occurrence match {
    case OccurrenceAt(namespace, cterm, occurrenceIndex) =>
      val term = ppCTerm(cterm).pp.unquote
      val idx = ppOccurrenceIndexSimple(occurrenceIndex)
      val nodeId = occurrenceToNodeId(occurrence)
      node(
        nodeId,
        'label -> html(
          literal(term),
          tag("sub",
            children = List(
              tag("font",
                attributes = List("point-size" -> "8"),
                children = List(
                  literal(idx)
                )
              )
            )
          )
        ),
        shapeRecord
      )
  }

  def occurrenceToNodeId(occurrence: Occurrence): NodeId = occurrence match {
    case OccurrenceAt(namespace, cterm, occurrenceIndex) =>
      val term = ppCTerm(cterm).pp.unquote
      val idx = ppOccurrenceIndexSimple(occurrenceIndex)
      nodeId(s""""${term}_${idx}"""")
  }

  def ppOccurrenceIndexSimple(occurrenceIndex: OccurrenceIndex): String = occurrenceIndex match {
    case OccurrenceIndexFrom(scope) => ppScopeName(scope).pp
    case OccurrenceTermIndex(prefix, termIndex) => termIndex.toString
  }

  def scopeNameToNodeId(scopeName: ScopeName): NodeId = nodeId(ppScopeName(scopeName).pp)
  def scopeNameToNode(scopeName: ScopeName): NodeStatement = node(scopeNameToNodeId(scopeName))
}

object RichDotDsl extends DotDsl{

}