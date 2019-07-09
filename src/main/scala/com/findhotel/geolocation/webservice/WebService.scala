package com.findhotel.geolocation.webservice


import akka.http.scaladsl.model.{StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RejectionHandler, Route}
import com.findhotel.geolocation.persistence.{GeoLocationPersistence}
import scala.concurrent.{ExecutionContextExecutor}
import scala.language.postfixOps
import com.findhotel.geolocation.webservice.controller.GeoLocationController


final class WebService(geoLocationPersistence: GeoLocationPersistence)(implicit executionContext: ExecutionContextExecutor) {

  private val totallyMissingHandler = RejectionHandler.newBuilder()
    .handleNotFound { complete((StatusCodes.NotFound, "Oh man, what you are looking for is long gone.")) }
    .result()

  private val geoLocationController = new GeoLocationController(geoLocationPersistence)

  val route: Route = handleRejections(totallyMissingHandler) {
    Routes.getGeoLocationRoute(geoLocationController)
  }
}

object Routes {

  def getGeoLocationRoute(controller: GeoLocationController)(implicit ec: ExecutionContextExecutor): Route =
    pathPrefix("api") {
      rejectEmptyResponse {
        redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) {
          path("locations") {
            pathEnd { get {
              parameters(('pageNumber.as[Long].?, 'pageSize.as[Long].?)).as(PageParams) { pageParams =>
                controller.getLocationEntities(pageParams)
              }
            }
            }
          } ~
            path("" / Remaining) { ipAddress =>
              get {
                controller.getLocationEntity(ipAddress)
              }
            }
        }
      }
    }

}
