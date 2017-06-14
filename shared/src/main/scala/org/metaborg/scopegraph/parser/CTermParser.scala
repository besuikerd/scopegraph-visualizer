package org.metaborg.scopegraph.parser

import scala.util.parsing.combinator._
import org.metaborg.scopegraph.ast._

import scala.util.matching.Regex

trait CTermParser extends RegexParsers with ImplicitConversions{
  import org.metaborg.util.Implicits._

  def cterm: Parser[CTerm] =
    cvar | cons | tuple | list | intTerm | stringTerm

  def cvar : Parser[CVar] = variable | wld | resolveVar
  def variable : Parser[Var] = varId ^^ Var
  def wld : Parser[Wld] = "_".r.map(_ => Wld())
  def resolveVar : Parser[ResolveVar] = ("?-" ~> varId <~ "-") ~ intLiteral ^^ ResolveVar

  def tuple : Parser[CTuple] = "(" ~> repsep(cterm, ",") <~ ")" ^^ CTuple
  def list : Parser[CList] = "[" ~> repsep(cterm, ",") <~ "]" ^^ CList
  def cons : Parser[CCons] = opId ~ ("(" ~> repsep(cterm, ",") <~ ")") ^^ CCons

  def intTerm: Parser[IntTerm] = intLiteral.map(_.toInt) ^^ IntTerm
  def stringTerm: Parser[StringTerm] = stringLiteral.map(_.unquote) ^^ StringTerm

  def stringLiteral: Parser[String] =
    ("\""+"""([^"\x00-\x1F\x7F\\]|\\[\\'"bfnrt]|\\u[a-fA-F0-9]{4})*"""+"\"").r


  def intLiteral : Regex = """\d+""".r
  def opId: Regex = "[A-Z][a-zA-Z0-9\\_]*".r
  def varId : Regex = """[a-z][a-zA-Z0-9\_]*[\']*""".r
}
