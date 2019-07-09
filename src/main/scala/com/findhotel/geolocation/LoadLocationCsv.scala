package com.findhotel.geolocation

import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong

import com.findhotel.geolocation.domain.Geolocation
import com.findhotel.geolocation.parser.{GeoLocationLoader, GeolocationParsingException}
import com.findhotel.geolocation.persistence.GeoLocationPersistence
import org.apache.commons.csv.{CSVFormat, CSVParser}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success, Try}


class LoadLocationCsv(database: GeoLocationPersistence,
                      filePath: String,
                      batchSize: Int)(implicit executionContext: ExecutionContextExecutor) {

  import LoadLocationCsv._

  val validRecordsCount = new AtomicLong(0l)
  val invalidRecordsCount = new AtomicLong(0l)
  var recordsRead = 0l
  val recordsBuffer = scala.collection.mutable.ListBuffer[Geolocation]()

  def readAndLoadLocations(): Try[Unit] = Try {

    val reader = Paths.get(filePath)
    val loader = GeoLocationLoader
    val csvParser = CSVParser.parse(reader, Charset.forName("UTF-8"), CSVFormat.DEFAULT
      .withFirstRecordAsHeader()
      .withIgnoreHeaderCase()
      .withTrim())

    csvParser.iterator().asScala.foreach { csvRecord =>
      loader.parseData(csvRecord) match {
        case Success(location) => recordsRead = recordsRead + 1
          if (recordsRead % batchSize != 0)
            recordsBuffer += location
          else storeToDB()
        case Failure(exp: GeolocationParsingException) => invalidRecordsCount.incrementAndGet()
          logger.debug(exp.msg)
        case exp: RuntimeException => logger.error("Error in location loading module", exp)
      }
    }
    storeToDB()
  }


  private def storeToDB(): Unit = {
    val recordToInsert = recordsBuffer.size
    validRecordsCount.addAndGet(recordToInsert)
    val array = new Array[Geolocation](recordToInsert)
    recordsBuffer.copyToArray(array)
    recordsBuffer.clear()
    println(s"Pushing Records To DB: $recordToInsert")
    storeToDB(array)
  }

  private def storeToDB(array: Array[Geolocation]): Unit = {
    database.addAllGeoLocations(array) onComplete {
      case Success(value) => logger.debug("record loaded")
      case Failure(exception) =>
        logger.error("Database exception", exception)
    }
  }
}

object LoadLocationCsv {

  import org.slf4j.{Logger, LoggerFactory}
  val logger: Logger = LoggerFactory.getLogger("LoadLocationCsv")

  def apply(database: GeoLocationPersistence,
            filePath: String,
            batchSize: Int = 10000)(implicit executionContext: ExecutionContextExecutor): LoadLocationCsv =
    new LoadLocationCsv(database, filePath, batchSize)
}
