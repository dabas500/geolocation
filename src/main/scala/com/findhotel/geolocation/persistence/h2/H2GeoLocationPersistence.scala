package com.findhotel.geolocation.persistence.h2

import akka.util.Timeout
import com.findhotel.geolocation.domain.Geolocation
import com.findhotel.geolocation.persistence.GeoLocationPersistence
import slick.lifted.TableQuery
import slick.jdbc.H2Profile.backend.Database
import slick.jdbc.H2Profile.api._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class H2GeoLocationPersistence(h2Database: Database)(
    implicit ec: ExecutionContext,
    timeout: Duration)
    extends GeoLocationPersistence {

  private lazy val geoLocationTable = TableQuery[GeoLocationTable]

  Await.result(h2Database.run(geoLocationTable.schema.create), timeout)

  private def fromBbReprGeoLocation(
      rows: Seq[GeoLocationDBRepr]): Seq[Geolocation] =
    rows.map(
      result =>
        Geolocation(result.ipAddress,
                    result.countryCode,
                    result.country,
                    result.city,
                    result.latitude,
                    result.longitude,
                    result.mysteryValue))

  override def getLocation(ipAddress: String): Future[Seq[Geolocation]] = {
    val getLocationByIpAddress =
      geoLocationTable.filter(data => data.ipAddress === ipAddress).result
    h2Database.run(getLocationByIpAddress).map(fromBbReprGeoLocation(_))
  }

  private def toDBReprGeoLocation(data: Geolocation): GeoLocationDBRepr = {
    GeoLocationDBRepr(0,
                      data.ipAddress,
                      data.countryCode,
                      data.country,
                      data.city,
                      data.latitude,
                      data.longitude,
                      data.mysteryValue)
  }
  override def addGeoLocation(data: Geolocation): Future[Int] = {
    val addGeoLocationQuery =
      geoLocationTable.insertOrUpdate(toDBReprGeoLocation(data))
    h2Database.run(addGeoLocationQuery)
  }

  override def getLocations(offset: Long,
                            limit: Long): Future[Seq[Geolocation]] = {
    val query = geoLocationTable
      .filter(row =>
        row.locationId >= offset && row.locationId <= (offset + limit))
      .result
    h2Database.run(query).map(fromBbReprGeoLocation(_))
  }

  override def addAllGeoLocations(rows: Seq[Geolocation]): Future[Int] = {
    val toBeInserted = rows.map { row =>
      TableQuery[GeoLocationTable].insertOrUpdate(toDBReprGeoLocation(row))
    }
    val inOneGo = DBIO.sequence(toBeInserted)
    h2Database.run(inOneGo.transactionally).map(_.sum)
  }

}
