package com.findhotel.geolocation

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.findhotel.geolocation.persistence.h2.H2GeoLocationPersistence
import com.findhotel.geolocation.webservice.WebService
import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration
import scala.util.Failure

object Main extends App {

  val filePath = System.getenv("FILE_PATH")

  if (filePath != null) {
    start(filePath)
  } else {
    println("FILE_PATH not defined.")
  }

  def start(path: String) {
    implicit val system: ActorSystem = ActorSystem("management-service")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    val slickDatabase = Database.forConfig("location-db")
    val config: Config = ConfigFactory.load()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val timeout = Duration.Inf

    val database = new H2GeoLocationPersistence(slickDatabase)

    val csvLoader = LoadLocationCsv(database, path)
    csvLoader.readAndLoadLocations() match {
      case scala.util.Success(value) =>
        println(
          s"Total Records Successfully loaded: ${csvLoader.validRecordsCount.get()}")
        println(s"Invalid Records: ${csvLoader.invalidRecordsCount.get()}")
      case Failure(exp) =>
        println(exp)
        system.terminate()
    }

    val route = new WebService(database).route

    Http().bindAndHandle(route, "0.0.0.0", 8083)
  }
}
