package org.metaborg.scopegraph.pp

import org.metaborg.pp.Doc
import org.metaborg.scopegraph.ast._

object CTermPrettyPrinter {
  import org.metaborg.pp.Doc._
  import Doc.Implicits._
  import ScopeGraphPrettyPrinter._

  def ppCTerm(cTerm: CTerm): Doc = cTerm match {
    case IntTerm(i) => i.toString
    case StringTerm(s) => s
    case CTuple(terms) => "(" <> group(terms.map(ppCTerm), ",") <> ")"
    case CList(terms) => "[" <> group(terms.map(ppCTerm), ",") <> "]"
    case CCons(name, subTerms) => name <> "(" <> group(subTerms.map(ppCTerm), ",") <> ")"
    case cVar: CVar => ppCVar(cVar)
    case occurrence: Occurrence => ppOccurrence(occurrence)
  }

  def ppCVar(cVar: CVar): Doc = cVar match {
    case Var(varId) => varId
    case Wld() => "_"
    case ResolveVar(lhs, index) => "?-" <> lhs <> "-" <> index
  }

}
