package org.metaborg.pp

import org.metaborg.dot.ast.DotDsl
import org.metaborg.dot.pp.DotPrettyPrinter
import org.metaborg.pp.Doc._

object DocTest extends App{

  sealed trait Tree
  case class Node(l: String, lhs: Tree, rhs: Tree) extends Tree
  case class Leaf(l: String) extends Tree


  def ppTree(tree: Tree): Doc = tree match{
    case Node(l, lhs, rhs) => text("Node(") <> text(l) $$ nest(2, ppNested(lhs) $$ ppNested(rhs))
    case Leaf(l) => text(l)
  }

  def ppNested(tree: Tree): Doc = tree match{
    case Leaf(l) => text("Leaf(") <> text(l) <> text(")")
    case t => text("(") <> ppTree(t) <> text(")")
  }


  val t =
    Node("root",
      Node("l1", Leaf("leaf1"), Leaf("leaf2")),
      Node("r1", Leaf("leaf3"), Leaf("leaf4"))
    )


  val t2 = text("Node \"foo\"") <> text(" ") <> (text("Node \"baz\" Leaf Leaf") $$ text("Node \"foobaz\" Leaf Leaf"))

  object RichDotDSL extends DotDsl
  import RichDotDSL._
  import Doc.Implicits._

  val dot = digraph(
    node("n1", shapeRecord),
    node("n2"),
    edge("n1", "n2"),
    subgraph("epic",

      node(nodeId("n3", "dsa", compass.n)),
      node("n4"),
      edge("n3", "n4")
    ),
    subgraph(attribute('rank -> "same"), nodeAttributes('shape -> "epic"),node("A"), node("B"), node("C"))
  )

//  println(DocPrinter.pp(ppTree(t)))
//  println(DocPrinter.pp(t2))
  println(DocPrinter.pp(DotPrettyPrinter.ppDot(dot)))

//  println(DocPrinter.pp("abc" <> ("d" $$ "e" $$ "f")))
//  println(DocPrinter.pp("abc" <> sep(List("d", "e","f"))))
//  println(DocPrinter.pp("abc {" <> newline <> nest(2, group(List("a", "b", "c"), newline)) $$ "}"))
}
