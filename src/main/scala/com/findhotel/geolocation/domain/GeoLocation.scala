package com.findhotel.geolocation.domain


final case class Geolocation(ipAddress: String,
                       countryCode: String,
                       country: String,
                       city: String,
                       latitude: String,
                       longitude: String,
                       mysteryValue: Long) extends Location

