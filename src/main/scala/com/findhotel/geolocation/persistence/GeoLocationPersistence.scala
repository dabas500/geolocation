package com.findhotel.geolocation.persistence

import com.findhotel.geolocation.domain.Geolocation
import scala.concurrent.Future

trait GeoLocationPersistence extends Persistence {
  def addGeoLocation(data: Geolocation): Future[Int]
  def getLocation(ipAddress: String): Future[Seq[Geolocation]]
  def getLocations(offset: Long, limit: Long): Future[Seq[Geolocation]]
  def addAllGeoLocations(rows: Seq[Geolocation]): Future[Int]

}
