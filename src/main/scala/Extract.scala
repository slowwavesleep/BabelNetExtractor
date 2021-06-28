import com.github.tototoshi.csv._

import java.io._
import scala.jdk.CollectionConverters._
import it.uniroma1.lcl.babelnet.{BabelNet, BabelSynsetIDRelation}
import it.uniroma1.lcl.babelnet.data.{BabelGloss, BabelPOS}
import it.uniroma1.lcl.jlt.util.Language
import it.uniroma1.lcl.babelnet.data.BabelPointer

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Extract {

  def main(args: Array[String]) = {

    val inFile = new File("src/main/resources/nouns_no_header.csv")
    val entriesFile = new File("src/main/resources/bn_entries.csv")
    val edgesFile = new File("src/main/resources/bn_edges.csv")

    val reader = CSVReader.open(inFile)
    val entryWriter = CSVWriter.open(entriesFile)
    val edgeWriter = CSVWriter.open(edgesFile)

    val bn = BabelNet.getInstance

    reader.foreach(fields => parseEntry(fields, bn, entryWriter, edgeWriter))
    reader.close()
    entryWriter.close()
  }

  def parseEntry(entry: Seq[Any], babelNet: BabelNet, entryWriter: CSVWriter, edgeWriter: CSVWriter): Unit = {

    val idLu: String = entry(1).toString
    val word: String = entry(2).toString
    val pos: String = entry(3).toString
    val fnDefinition: String = entry(4).toString

    val synsets = babelNet.getSynsets(word, Language.EN, BabelPOS.NOUN).asScala

    for (synset <- synsets) {

      val glosses = synset.getGlosses(Language.EN).asScala

      val entryId: String = synset.getId.toString
      val entryName: String = synset.toString
      val entrySource: String = synset.getSynsetSource.toString
      val bnDefinition: String = getDefinition(glosses)
//      val edges: String = edgesToString(synset.getEdges(BabelPointer.HYPERNYM).asScala)
      val edges: String = edgesToString(synset.getEdges.asScala)

      val extractedEntry: List[String] = List(
        idLu, word, pos, fnDefinition, entryId, entryName, entrySource, bnDefinition
      )

      val entryRelations: List[String] = List(entryId, edges)

      entryWriter.writeRow(extractedEntry)
      edgeWriter.writeRow(entryRelations)
    }

    def edgesToString(edges: mutable.Buffer[BabelSynsetIDRelation]): String = {
      val relations = new ListBuffer[String]
      for (edge <- edges) {
        relations += edge.getPointer.toString + "|" + edge.getBabelSynsetIDTarget.toString
      }
      relations.mkString("||")
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
