import org.metaborg.scopegraph.parser.ScopeGraphParser
import org.scalatest.FlatSpec

class ParseSpec extends FlatSpec{
  "parser" should "parse scope names" in {

    val s =
      """scope graph {
        |  scope #Ex1_1_unlabeled.w-sce-1 {
        |    declarations {
        |      AST{Ref("x") @Ex1_1_unlabeled.w:3}
        |    }
        |    direct edges {
        |      P ~ #Ex1_1_unlabeled.w-sce-2
        |    }
        |  }
        |}
      """.stripMargin

    val scopegraph = ScopeGraphParser.parse(s).fold(
      error => fail(error),
      identity
    )
  }

}
