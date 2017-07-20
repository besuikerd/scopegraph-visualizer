import org.metaborg.json.pp.JSONPrettyPrinter
import org.metaborg.scopegraph.parser.ScopeGraphParser
import org.metaborg.vis.ScopegraphToVis
import org.scalatest.FlatSpec

class VisSpec extends FlatSpec{
  "scopegraph" should "transform to vis format" in {
    import org.metaborg.pp.Doc.Implicits._

    val scopegraph = ScopeGraphParser.parse(TestFiles.test1).fold(
      error => fail(error),
      identity
    )
    val visDataset = ScopegraphToVis.scopegraphToVisDataset(scopegraph)
    val json = ScopegraphToVis.visDataSetToJSON(visDataset)
    val pp = JSONPrettyPrinter.ppJSON(json).pp
  }


}
