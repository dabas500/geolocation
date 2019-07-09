package com.findhotel.geolocation.webservice.controller

import akka.http.scaladsl.server.Directives.{complete, onSuccess}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.findhotel.geolocation.domain
import com.findhotel.geolocation.domain.{Geolocation, Location}
import com.findhotel.geolocation.persistence.GeoLocationPersistence
import com.findhotel.geolocation.webservice.PageParams
import spray.json.DefaultJsonProtocol
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

class GeoLocationController(persistence: GeoLocationPersistence) {
  object JsonDomainObjects extends domain.json with DefaultJsonProtocol
  import JsonDomainObjects._
  import spray.json.DefaultJsonProtocol
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit val timeout: Timeout = 5 seconds

  def getLocationEntities(pageParams: PageParams)(
      implicit ec: ExecutionContextExecutor): Route = {

    val items: Future[Seq[Geolocation]] =
      persistence.getLocations(pageParams.skip, pageParams.limit)
    onSuccess(items)(complete(_))
  }

  def getLocationEntity(ipAddress: String)(
      implicit ec: ExecutionContextExecutor): Route = {
    val items: Future[Seq[Geolocation]] = persistence.getLocation(ipAddress)
    onSuccess(items)(complete(_))
  }
}
