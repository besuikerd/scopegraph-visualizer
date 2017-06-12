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

  def ppOccurrenceIndex(occurrenceIndex: OccurrenceIndex): Doc = occurrenceIndex match {
    case OccurrenceIndexFrom(scope) => "@" <> ppScopeName(scope)
    case OccurrenceTermIndex(prefix, termIndex) => "@" <> prefix <> ":" <> termIndex.toString
  }

  def ppScopeName(scopeName: ScopeName): Doc = scopeName.name <> scopeName.index
}
