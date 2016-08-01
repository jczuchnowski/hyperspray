package org.hyperspray.cj.route

import spray.http._
import spray.http.HttpHeaders._
import spray.http.MediaTypes._

object CollectionJsonRoute {
  
  val `application/vnd.collection+json` = register(
    MediaType.custom(
      mainType = "application",
      subType = "vnd.collection+json",
      compressible = true,
      binary = true,
      fileExtensions = Seq.empty))
}
