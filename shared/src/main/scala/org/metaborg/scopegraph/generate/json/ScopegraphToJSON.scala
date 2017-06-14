package org.metaborg.scopegraph.generate.json

import org.metaborg.json.ast.JSON
import org.metaborg.scopegraph.ast.ScopePartEntry.SplitEntries
import org.metaborg.scopegraph.ast._

object ScopegraphToJSON {
  import JSON._
  import JSON.Implicits._

  def scopegraphToJSON(scopegraph: ScopeGraph): JSON =
    obj(
      "scopes" -> scopegraph.scopeEntries.map(scopeEntryToJSON)
    )

  def scopeEntryToJSON(scopeEntry: ScopeEntry): JSON = scopeEntry match {
    case Scope(scopeName, scopePartEntries) => {
      val SplitEntries(
        declarations,
        references,
        directEdges,
        importEdges,
        assocEdges
      ) = ScopePartEntry.splitEntries(scopePartEntries)
      obj(
        "id" -> scopeNameToJSON(scopeName),
        "declarations" -> declarations.map(declEntryToJSON),
        "references" -> references.map(refEntryToJSON),
        "directEdges" -> directEdges.map(directEntryToJSON),
        "importEdges" -> importEdges.map(importEntryToJSON),
        "assocEdges" -> assocEdges.map(assocEntryToJSON)
      )
    }
  }

  def scopeNameToJSON(scopeName: ScopeName): JSON = obj(
    "name" -> scopeName.name,
    "index" -> scopeName.index
  )

  def declEntryToJSON(declEntry: DeclEntry): JSON = declEntry match {
    case Decl(occurrence, declType) => obj(
      "occurrence" -> occurrenceToJSON(occurrence),
      "type" -> declTypeToJSON(declType)
    )
  }

  def declTypeToJSON(declType: DeclType): JSON = declType match {
    case Type(cTerm) => cTermToJSON(cTerm)
    case NoType() => nil
  }

  def refEntryToJSON(refEntry: RefEntry): JSON = refEntry match {
    case Ref(occurrence) => obj(
      "occurrence" -> occurrenceToJSON(occurrence)
    )
  }

  def directEntryToJSON(directEntry: DirectEntry): JSON = directEntry match {
    case DirectEdge(pathLabel, scopeName) => obj(
      "label" -> pathLabel,
      "scopeName" -> scopeNameToJSON(scopeName)
    )
  }

  def importEntryToJSON(importEntry: ImportEntry): JSON = importEntry match {
    case ImportEdge(pathLabel, occurrence) => obj(
      "label" -> pathLabel,
      "occurrence" -> occurrenceToJSON(occurrence)
    )
  }

  def assocEntryToJSON(assocEntry: AssocEntry): JSON = assocEntry match {
    case AssocEdge(pathLabel, occurrence) => obj(
      "label" -> pathLabel,
      "occurrence" -> occurrenceToJSON(occurrence)
    )
  }

  def occurrenceToJSON(occurrence: Occurrence): JSON = occurrence match {
    case OccurrenceAt(namespace, cTerm, occurrenceIndex) =>
      obj(
        "namespace" -> namespace,
        "name" -> cTermToJSON(cTerm),
        "index" -> occurrenceIndexToJSON(occurrenceIndex)
      )
  }

  def occurrenceIndexToJSON(occurrenceIndex: OccurrenceIndex): JSON = occurrenceIndex match {
    case OccurrenceIndexFrom(scopeName) => scopeNameToJSON(scopeName)
    case OccurrenceTermIndex(prefix, termIndex) => obj(
      "prefix" -> prefix,
      "index" -> termIndex
    )
  }

  def cTermToJSON(cTerm: CTerm): JSON = cTerm match {
    case IntTerm(i) => i
    case StringTerm(s) => s
    case CTuple(terms) => array(terms.map(cTermToJSON))
    case CList(terms) => array(terms.map(cTermToJSON))
    case CCons(name, subTerms) => obj(
      "name" -> name,
      "subTerms" -> subTerms.map(cTermToJSON)
    )
    case cVar: CVar => cVarToJSON(cVar)
    case occurrence: Occurrence => occurrenceToJSON(occurrence)
  }

  def cVarToJSON(cVar: CVar): JSON = cVar match {
    case Var(varId) => obj(
      "type" -> "var",
      "var" -> varId
    )
    case Wld() => obj(
      "type" -> "wildcard"
    )
    case ResolveVar(lhs, index) => obj(
      "type" -> "resolveVar",
      "name" -> lhs,
      "index" -> index
    )
  }
}
