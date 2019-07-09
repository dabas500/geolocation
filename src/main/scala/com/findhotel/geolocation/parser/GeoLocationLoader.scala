package com.findhotel.geolocation.parser


import java.util.regex.Pattern
import com.findhotel.geolocation.domain.Geolocation
import org.apache.commons.csv.CSVRecord
import scala.util.{Failure, Success, Try}


object GeoLocationLoader {

  private val INET_REGX = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})")
  private val COUNTRY_CODE_REGX = Pattern.compile("([aA-zZ]{1,2})")
  private val LATITUDE_REGX = Pattern.compile("(\\+|-)?(?:90(?:(?:\\.0*)?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]+)?))")
  private val LONGITUDE_REGX = Pattern.compile("(\\+|-)?(?:180(?:(?:\\.0*)?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]+)?))")

  def parseData(data: CSVRecord): Try[Geolocation] = {
    for {
      validatedInet        <- Try(data.get("ip_address")).flatMap(inet => validateInet(inet))
      validatedCountryCode <- Try(data.get("country_code")).flatMap(countryCode=> validateCountryCode(countryCode))
      validatedLatitude    <- Try(data.get("latitude")).flatMap(latitude => validateLatitude(latitude))
      validatedLongitude    <- Try(data.get("longitude")).flatMap(longitude=> validateLongitude(longitude))
      validatedMysteryValue  <- Try(data.get("mystery_value").toLong)
      country <- Try(data.get("country"))
      city <- Try(data.get("city"))
    } yield Geolocation(validatedInet, validatedCountryCode, country, city, validatedLatitude, validatedLongitude, validatedMysteryValue)
  }

  private def validateInet(inet: String): Try[String] =
    if (INET_REGX.matcher(inet).matches()) Success(inet)
    else Failure(GeolocationParsingException(s"Error parsing Inet $inet"))

  private def validateLatitude(latitude: String): Try[String] =
    if (LATITUDE_REGX.matcher(latitude).matches()) Success(latitude)
    else Failure(GeolocationParsingException(s"Error parsing latitude $latitude"))

  private def validateLongitude(longitude: String): Try[String] = if (LONGITUDE_REGX.matcher(longitude).matches()) Success(longitude)
  else Failure(GeolocationParsingException(s"Error parsing longitude $longitude"))

  private def validateCountryCode(countryCode: String): Try[String] = if (COUNTRY_CODE_REGX.matcher(countryCode).matches()) Success(countryCode)
  else Failure(GeolocationParsingException(s"Error parsing country code $countryCode"))

}