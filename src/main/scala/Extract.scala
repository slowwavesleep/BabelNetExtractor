import com.github.tototoshi.csv._

import java.io._
import scala.jdk.CollectionConverters._
import it.uniroma1.lcl.babelnet.{BabelNet, BabelSynsetIDRelation}
import it.uniroma1.lcl.babelnet.data.{BabelGloss, BabelPOS}
import it.uniroma1.lcl.jlt.util.Language

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Extract {

  def main(args: Array[String]) = {

    val inFile = new File("src/main/resources/nouns_no_header.csv")
    val outFile = new File("src/main/resources/out.csv")

    val reader = CSVReader.open(inFile)
    val writer = CSVWriter.open(outFile, append = false)

    val bn = BabelNet.getInstance

    reader.foreach(fields => parseLine(fields, bn, writer))
    reader.close()
    writer.close()
  }

  def parseLine(line: Seq[Any], babelNet: BabelNet, writer: CSVWriter): Unit = {

    val idLu: String = line(1).toString
    val word: String = line(2).toString
    val pos: String = line(3).toString
    val fnDefinition: String = line(4).toString

    val synsets = babelNet.getSynsets(word, Language.EN, BabelPOS.NOUN).asScala

    for (synset <- synsets) {

      val glosses = synset.getGlosses(Language.EN).asScala

      val entryId: String = synset.getId.toString
      val entryName: String = synset.toString
      val entrySource: String = synset.getSynsetSource.toString
      val bnDefinition: String = getDefinition(glosses)
      val edges: String = edgesToString(synset.getEdges.asScala)

      val extractedEntry: List[String] = List(
        idLu, word, pos, fnDefinition, entryId, entryName, entrySource, bnDefinition, edges
      )

      writer.writeRow(extractedEntry)

    }

    def edgesToString(edges: mutable.Buffer[BabelSynsetIDRelation]): String = {
      val edgeIds = new ListBuffer[String]
      for (edge <- edges) {
        edgeIds += edge.getBabelSynsetIDTarget.toString
      }
      edgeIds.mkString("|")
    }



    def getDefinition(glosses: mutable.Buffer[BabelGloss]): String = {
      if (glosses.isEmpty) {
        ""
      } else {
        glosses.head.toString
      }
    }



  }



}
