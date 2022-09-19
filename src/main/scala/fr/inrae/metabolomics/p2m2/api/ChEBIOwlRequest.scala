package fr.inrae.metabolomics.p2m2.api

import fr.inrae.metabohub.semantic_web.SWDiscovery
import fr.inrae.metabohub.semantic_web.configuration.SWDiscoveryConfiguration
import fr.inrae.metabohub.semantic_web.rdf.URI

import java.io._
import java.util.zip.GZIPInputStream
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

case object ChEBIOwlRequest  {

  def load(path: String,tempDir:String) = {

    if (! new File(s"$tempDir/chebi.owl").exists()) {
      println(s"== $tempDir == ")
      Try(new File(s"$tempDir").mkdir()) match {
        case Success(_) =>
          println("== download chebi ==")
          val v=new java.net.URL(path)
          println("== unzip ==")
          val inputStream = new GZIPInputStream(new ByteArrayInputStream(v.openStream().readAllBytes()))
          Try(scala.io.Source.fromInputStream(inputStream).mkString) match {
            case Success(owl) =>
              val bw = new BufferedWriter(new FileWriter(new File(s"$tempDir/chebi.owl")))
              bw.write(owl)
              bw.close()
            case Failure(exception) => throw exception
          }

        case Failure(e) => throw e
      }

    }
    println("== chebi ok==")
  }
  /*
  https://ftp.ebi.ac.uk/pub/databases/chebi/ontology/chebi_lite.owl.gz
  https://ftp.ebi.ac.uk/pub/databases/chebi/ontology/chebi.owl.gz
  http://purl.obolibrary.org/obo/chebi.owl.gz
   */

  def discovery(path:String) : SWDiscovery = {
    SWDiscovery(SWDiscoveryConfiguration().localFile(path,"text/rdf-xml"))
      .prefix("chebi","http://purl.obolibrary.org/obo/chebi/")
      .something("compound")
        .isSubjectOf(URI("chebi:monoisotopicmass"),"v")
      .set("594.27299")
      .console
  }

  def get(id: String)(implicit path: String="https://ftp.ebi.ac.uk/pub/databases/chebi/ontology/chebi.owl.gz", tempDir: String="./ChEBIOwl") = {
    load(path,tempDir)
    println("REQUEST................")
    val r = Await.ready(discovery(tempDir+"/chebi.owl").select(Seq("v")).limit(10).commit().raw, Duration.Inf)

     println(r)
    println("END")

  }
}
