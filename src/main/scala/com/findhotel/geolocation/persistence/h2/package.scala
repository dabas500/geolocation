package com.findhotel.geolocation.persistence

/***
  * The class represents the database mapping of geolocation , to build typesafe queries.
  */
package object h2 {
  import slick.lifted.{Rep, Tag}
  import slick.jdbc.H2Profile.api._

  private[h2] case class GeoLocationDBRepr(locationId: Long,
                                           ipAddress: String,
                                           countryCode: String,
                                           country: String,
                                           city: String,
                                           latitude: String,
                                           longitude: String,
                                           mysteryValue: Long)

  final class GeoLocationTable(tag: Tag)
      extends Table[GeoLocationDBRepr](tag, "TOPIC") {
    def locationId: Rep[Long] =
      column[Long]("LOCATION_ID", O.PrimaryKey, O.AutoInc)
    def ipAddress: Rep[String] = column[String]("IP_ADDRESS")
    def countryCode: Rep[String] = column[String]("COUNTRY_CODE")
    def country: Rep[String] = column[String]("COUNTRY")
    def city: Rep[String] = column[String]("CITY")
    def latitude: Rep[String] = column[String]("LATITUDE")
    def longitude: Rep[String] = column[String]("LONGITUDE")
    def mysteryValue: Rep[Long] = column[Long]("MYSTERY_VALUE")
    override def * =
      (locationId,
       ipAddress,
       countryCode,
       country,
       city,
       latitude,
       longitude,
       mysteryValue).mapTo[GeoLocationDBRepr]
  }

}
