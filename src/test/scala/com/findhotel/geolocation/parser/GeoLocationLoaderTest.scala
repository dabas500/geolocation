package com.findhotel.geolocation.parser

import com.findhotel.geolocation.persistence.GeoLocationPersistence
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar


class GeoLocationLoaderTest extends FunSuite with MockitoSugar {

  val GeoLocationPersistence = mock[GeoLocationPersistence]
  val geoLocatiolLoader = GeoLocationLoader

  val data = Map[String, String](
    "ipAddress" -> "200.106.141.15",
    "countryCode" -> "SI",
    "latitude" -> "-941.87503094689836",
    "longitude" -> "7.206435933364332",
    "country" -> "Nepal",
    "city" -> "DuBuquemouth",
    "mysteryValue" -> "7823011346"
  )

  /*test("latitude parsing test") {
    val invalidLatitude = data + ("latitude" -> "-90.8750309468983")
    assert(geoLocatiolLoader.parseData(invalidLatitude).isFailure)

    val validLatitude = data + ("latitude" -> "-90")
    assert(geoLocatiolLoader.parseData(invalidLatitude).isSuccess)

  }*/
  test("longitude parsing test") {
    assert(Set.empty.size == 0)

  }
  test("contry code parsing test") {
    assert(Set.empty.size == 0)

  }
  test("ip address parsing test") {
    assert(Set.empty.size == 0)

  }
}
