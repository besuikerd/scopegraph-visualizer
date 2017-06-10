package org.metaborg.scopegraph.parser

import org.metaborg.scopegraph.ast._

import scala.util.matching.Regex
import scala.util.parsing.combinator.{ImplicitConversions, RegexParsers}
import scala.util.parsing.input.CharSequenceReader

trait ScopeGraphParser extends RegexParsers with CTermParser with ImplicitConversions{
  override def cterm: Parser[CTerm] =
    occurrence | super.cterm


  def scopeGraph : Parser[ScopeGraph] = "scope graph" ~> ("{" ~> rep(scopeEntry) <~ "}") ^^ ScopeGraph

  def scopeEntry : Parser[ScopeEntry] = scope
  def scope : Parser[Scope] = ("scope" ~> scopeName) ~ ("{" ~> rep(scopePartEntry) <~ "}") ^^ Scope

  def scopePartEntry : Parser[ScopePartEntry] =
      decls | refs | directEdges | importEdges | assocEdges

  def refs : Parser[Refs] = "references" ~> blockRep(refEntry) ^^ Refs
  def decls : Parser[Decls] = "declarations" ~> blockRep(declEntry) ^^ Decls
  def directEdges : Parser[DirectEdges] = "direct edges" ~> blockRep(directEntry) ^^ DirectEdges
  def importEdges : Parser[ImportEdges] = "import edges" ~> blockRep(importEntry) ^^ ImportEdges
  def assocEdges : Parser[AssocEdges] = "associated declarations" ~> blockRep(assocEntry) ^^ AssocEdges

  def refEntry : Parser[RefEntry] = ref
  def ref : Parser[Ref] = occurrence ^^ Ref

  def declEntry : Parser[DeclEntry] = decl
  def decl : Parser[Decl] = occurrence ~ declType ^^ Decl

  def declType : Parser[DeclType] = declAType | declNoType
  def declAType : Parser[Type] = ":" ~> cterm ^^ Type
  def declNoType : Parser[NoType] = success(NoType())

  def directEntry : Parser[DirectEntry] = directEdge
  def directEdge : Parser[DirectEdge] = labelTilde ~ scopeName ^^ DirectEdge

  def importEntry : Parser[ImportEntry] = importEdge
  def importEdge : Parser[ImportEdge] = labelTilde ~ occurrence ^^ ImportEdge

  def assocEntry : Parser[AssocEntry] = assocEdge
  def assocEdge : Parser[AssocEdge] = labelTilde ~ occurrence ^^ AssocEdge

  def scopeName : Parser[ScopeName] = "#-" ~> (varId <~ "-") ~ (varId | intLiteral) ^^ ScopeName

  def occurrence : Parser[Occurrence] = occurrenceAt
  def occurrenceAt : Parser[OccurrenceAt] = namespaceRef ~ ("{" ~> cterm) ~ (occurrenceIndex <~ "}") ^^ OccurrenceAt

  def occurrenceIndex : Parser[OccurrenceIndex] = occurrenceIndexFrom | occurrenceTermIndex
  def occurrenceIndexFrom : Parser[OccurrenceIndexFrom] = "@" ~> scopeName ^^ OccurrenceIndexFrom
  def occurrenceTermIndex : Parser[OccurrenceTermIndex] = ("@" ~> (varId | "") <~ ":") ~ intLiteral.map(_.toInt) ^^ OccurrenceTermIndex

  def block[A](p: Parser[A]): Parser[A] = "{" ~> p <~ "}"
  def blockRep[A](p:Parser[A]):Parser[List[A]] = block(rep(p))

  def labelId : Regex = """[A-Z]+""".r
  def labelTilde = labelId <~ "~"

  val namespaceRef : Regex = """[A-Z][a-zA-Z0-9]*""".r
}

object ScopeGraphParser{
  val instance: ScopeGraphParser = new ScopeGraphParser {}

  def parse(input: String): Either[String, ScopeGraph] = {
    instance.parse(instance.scopeGraph, new CharSequenceReader(input)) match{
      case instance.Success(scopegraph: ScopeGraph, _) => Right(scopegraph)
      case instance.NoSuccess(msg, _) => Left(msg)
    }
  }
}
