package com.findhotel.geolocation

package object domain {

    trait Location
    trait json {
            import spray.json.DefaultJsonProtocol._
            implicit val topicToSchemaFormat = jsonFormat7(Geolocation)
        }
}
