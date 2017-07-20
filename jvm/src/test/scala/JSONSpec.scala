import org.metaborg.json.ast.JSON
import org.metaborg.json.pp.JSONPrettyPrinter
import org.metaborg.pp.Doc
import org.metaborg.scopegraph.ast.ScopeGraph
import org.metaborg.scopegraph.generate.json.ScopegraphToJSON
import org.metaborg.scopegraph.parser.ScopeGraphParser
import org.scalatest.{FlatSpec, Ignore}

import scala.io.Source

@Ignore
class JSONSpec extends FlatSpec{
  import Doc.Implicits._
  import JSON.Implicits._
  import JSON._



  "json" should "pp"  in {


    val json = obj(
      "x" -> obj(
        "y" -> 1,
        "z" -> array(
          1,
          true,
          obj(),
          array()
        )
      )
    )

    val expected =
      """
        |{
        |  "x": {
        |    "y": 1,
        |    "z": [
        |      1,
        |      true,
        |      {},
        |      []
        |    ]
        |  }
        |}
      """.trim.stripMargin

    val pp = JSONPrettyPrinter.ppJSON(json).pp
//    assertResult(expected)(pp)
  }

  "scopegraph" should "be converted to json" in {


    val source = Source.fromResource("test1.scopegraph").mkString
    val scopegraph: ScopeGraph = ScopeGraphParser.parse(source).fold(
      error => fail(error),
      identity
    )
    val json = ScopegraphToJSON.scopegraphToJSON(scopegraph)
    val expected = Source.fromResource("test1.json").mkString
//    assertResult(expected)(JSONPrettyPrinter.ppJSON(json).pp)
  }



}
