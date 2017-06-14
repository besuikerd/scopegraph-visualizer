import org.metaborg.json.ast.JSON
import org.metaborg.json.pp.JSONPrettyPrinter
import org.metaborg.pp.Doc
import org.scalatest.FlatSpec

class JSONSpec extends FlatSpec{
  import Doc.Implicits._
  import JSON.Implicits._
  import JSON._

  "json" should "pp" in {


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
    assertResult(expected)(pp)
  }
}
