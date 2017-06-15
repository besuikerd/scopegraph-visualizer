package org.metaborg.pp

sealed trait Doc extends RichDocOptions{
  def <>(rhs: Doc): Doc = H(this, rhs)
  def $$(rhs: Doc): Doc = V(this, rhs)
}

trait RichDocOptions{ this: Doc =>
  import Doc._
  def <^>(that: Doc) = this <> text(" ") <> that
}


class DocContext(
  var h: Int,
  var v: Int,
  var hOffset: Int,
  val builder: StringBuilder,
  val hLimit: Int = 80
)

object DocPrinter{


  def pp(doc: Doc): String = {
    val ctx = new DocContext(0, 0, 0, StringBuilder.newBuilder)
    pp(doc, ctx)
    ctx.builder.toString()
  }

  private def pp(doc: Doc, ctx: DocContext): Unit = {
    doc match {
      case V(lhs, rhs) => {
        val h = ctx.h
        pp(lhs, ctx)
        crnl(ctx, h)
        pp(rhs, ctx)
      }
      case H(lhs, rhs) => {
        pp(lhs, ctx)
        pp(rhs, ctx)
      }
      case Text(s) => {
        var remainder = s
        while(remainder.nonEmpty){
          val charsHorizontal = Math.min(ctx.hLimit - ctx.h, remainder.length)
          val (toAppend, rest) = remainder.splitAt(charsHorizontal)
          ctx.builder.append(toAppend)
          remainder = rest
          if(remainder.nonEmpty){
            crnl(ctx)
          } else {
            ctx.h += charsHorizontal
          }
        }
      }
      case Sep(docs) => {
        if(docs.nonEmpty){
          pp(docs.reduceLeft(_ $$ _), ctx)
        }
      }
      case Nest(n, doc) => {
        ctx.hOffset += n
        if(ctx.h < ctx.hOffset) {
          linefeed(ctx, ctx.hOffset - ctx.h)
        }
        pp(doc, ctx)
        ctx.hOffset -= n
      }
      case Newline() => {
        crnl(ctx)
      }
      case Empty() =>
    }
  }

  private def crnl(ctx: DocContext): Unit = crnl(ctx, ctx.hOffset)
  private def crnl(ctx: DocContext, h: Int): Unit ={
    ctx.v += 1
    ctx.h = 0
    ctx.builder.append('\n')
    linefeed(ctx, h)
  }
  private def linefeed(ctx: DocContext, h: Int): Unit = {
    ctx.h += h
    ctx.builder.append(" " * h)
  }
}


case class V(lhs: Doc, rhs: Doc) extends Doc
case class H(lhs: Doc, rhs: Doc) extends Doc
case class Text(s: String) extends Doc
case class Sep(docs: List[Doc]) extends Doc
case class Nest(n: Int, doc: Doc) extends Doc
case class Empty() extends Doc
case class Newline() extends Doc
object Doc{
  def text(s: String): Doc = Text(s)
  def sep(docs: List[Doc]): Doc = Sep(docs)
  def sep2(docs: Doc*): Doc = sep(docs.toList)

  def nest(n: Int, doc: Doc): Doc = Nest(n, doc)
  def group(docs: List[Doc], sep: Doc): Doc =
    if(docs.isEmpty)
      empty
    else
      docs.reverse.reduceLeft[Doc]{
        case (acc, cur) => cur <> sep <> acc
      }
  val newline: Doc = Newline()
  val empty = Empty()

  object Implicits {
    implicit def string2Doc(s: String) = text(s)
    implicit def numeric2Doc[N](n: N)(implicit numeric: Numeric[N]) = text(n.toString)

    implicit class RichDoc(val doc: Doc) extends AnyRef{
      def pp: String = DocPrinter.pp(doc)
    }
  }
}
