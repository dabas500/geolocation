package com.findhotel.geolocation.webservice


import akka.http.scaladsl.server.Route
import com.findhotel.geolocation.domain.Geolocation
import com.findhotel.geolocation.persistence.GeoLocationPersistence
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{ImplicitSender, TestKit}
import com.findhotel.geolocation.domain
import com.findhotel.geolocation.webservice.controller.GeoLocationController
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec, WordSpecLike}
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future
import scala.concurrent.Future


class GeoLocationControllerTest extends  WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  implicit val context = system.dispatcher
  import spray.json.DefaultJsonProtocol
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  object JsonDomainObjects extends  domain.json with DefaultJsonProtocol
  import JsonDomainObjects._

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val persistence = mock[GeoLocationPersistence]
  val controller = new GeoLocationController(persistence)
  val locationURl = "/api/locations"
  val ipAddress = "1.22.12.12"
  val geoLocation = Geolocation(ipAddress,
    "SI",
    "Nepal",
    "DuBuquemouth",
    "-84.87503094689836",
    "7.206435933364332",
    7823011346L)


  "GeoLocation getAll entities" must {
    "Return Seq of GeoLocations" in {

      when(persistence.getLocations(0,16))
        .thenReturn(Future.successful(Seq(geoLocation)))

      Get(s"$locationURl") ~> Routes.getGeoLocationRoute(controller) ~> check {
        responseAs[Seq[Geolocation]] shouldEqual Seq(geoLocation)
      }
    }

  }
}
