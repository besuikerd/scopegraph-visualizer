package org.metaborg.scopegraph.pp

import org.metaborg.pp.Doc
import org.metaborg.scopegraph.ast._

object ScopeGraphPrettyPrinter {
  import org.metaborg.pp.Doc._
  import Doc.Implicits._
  import CTermPrettyPrinter._

  def ppOccurrence(occurrence: Occurrence): Doc = occurrence match {
    case OccurrenceAt(namespace, cTerm, occurrenceIndex) => namespace <> ppCTerm(cTerm) <> ppOccurrenceIndex(occurrenceIndex)
  }

  def ppHumanReadableOccurrence(occurrence: Occurrence): Doc = occurrence match {
    case OccurrenceAt(namespace, cTerm, occurrenceIndex) =>
      if(namespace.nonEmpty)
        namespace <> "{" <> ppCTerm(cTerm) <> "}" <> "@" <> ppHumanReadableOccurrenceIndex(occurrenceIndex)
      else
        ppCTerm(cTerm) <> "@" <> ppHumanReadableOccurrenceIndex(occurrenceIndex)
  }

  def ppOccurrenceIndex(occurrenceIndex: OccurrenceIndex): Doc = occurrenceIndex match {
    case OccurrenceIndexFrom(scope) => "@" <> ppScopeName(scope)
    case OccurrenceTermIndex(prefix, termIndex) => "@" <> prefix <> ":" <> termIndex.toString
  }

  def ppHumanReadableOccurrenceIndex(occurrenceIndex: OccurrenceIndex): Doc = occurrenceIndex match {
    case OccurrenceIndexFrom(scopeName) => ppScopeName(scopeName)
    case OccurrenceTermIndex(prefix, termIndex) =>
      if(prefix.nonEmpty)
        prefix <> ":" <> termIndex
      else
        termIndex
  }

  def ppScopeName(scopeName: ScopeName): Doc = scopeName.name <> scopeName.index
}
