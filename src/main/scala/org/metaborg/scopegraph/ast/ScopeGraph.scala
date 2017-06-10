package org.metaborg.scopegraph.ast

case class ScopeGraph(scopeEntries: List[ScopeEntry])

sealed trait ScopeEntry
case class Scope(scope: ScopeName, scopePartEntries: List[ScopePartEntry]) extends ScopeEntry

sealed trait ScopePartEntry
case class Decls(declEntries: List[DeclEntry]) extends ScopePartEntry
case class Refs(refEntries: List[RefEntry]) extends ScopePartEntry
case class DirectEdges(directEdges: List[DirectEntry]) extends ScopePartEntry
case class ImportEdges(importEdges: List[ImportEntry]) extends ScopePartEntry
case class AssocEdges(assocEdges: List[AssocEntry]) extends ScopePartEntry

sealed trait DeclEntry
case class Decl(occurrence: Occurrence, declType: DeclType) extends DeclEntry

sealed trait RefEntry
case class Ref(occurrence: Occurrence) extends RefEntry

sealed trait DirectEntry
case class DirectEdge(pathLabel: String, scope: ScopeName) extends DirectEntry

sealed trait ImportEntry
case class ImportEdge(pathLabel: String, occurrence: Occurrence) extends ImportEntry

sealed trait AssocEntry
case class AssocEdge(pathLabel: String, occurrence: Occurrence) extends AssocEntry

sealed trait DeclType
case class Type(cTerm: CTerm) extends DeclType
case class NoType() extends DeclType

sealed trait CTerm
case class IntTerm(i: Int) extends CTerm
case class StringTerm(s: String) extends CTerm
case class CTuple(terms: List[CTerm]) extends CTerm
case class CList(terms: List[CTerm]) extends CTerm
case class CCons(name: String, subTerms: List[CTerm]) extends CTerm


sealed trait CVar extends CTerm
case class Var(varId: String) extends CVar
case class Wld() extends CVar
case class ResolveVar(lhs: String, index: String) extends CVar


sealed trait Occurrence extends CTerm
case class OccurrenceAt(namespace: String, cterm: CTerm, occurrenceIndex: OccurrenceIndex) extends Occurrence


sealed trait OccurrenceIndex
case class OccurrenceIndexFrom(scope: ScopeName) extends OccurrenceIndex
case class OccurrenceTermIndex(prefix: String, termIndex: Int) extends OccurrenceIndex


case class ScopeName(name: String, index: String)
