package com.findhotel.geolocation


package object webservice {

  private [webservice] case class PageParams(pageNumber: Option[Long],
                        pageSize: Option[Long]) {
    require(pageNumber.forall(size => size > 0), "page number must be greater then 0")
    require(pageSize.forall(size => size > 0 && size < 1000), "page size must be greater then 0 and less then 1000")

    private val DefaultPageNumber = 1l
    private val DefaultPageSize = 16l

    val limit = pageSize.getOrElse(DefaultPageSize)
    val skip= (pageNumber.getOrElse(DefaultPageNumber) - 1l) * limit
  }
}
