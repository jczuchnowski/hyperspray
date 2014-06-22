package org.hyperspray.collectionjson.route

import java.net.URI
import org.collectionjson.Builder
import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._
import org.collectionjson.model._
import org.hyperspray.collectionjson.route.CollectionJsonProtocol._
import spray.httpx.SprayJsonSupport._
import spray.routing.Directives
import spray.http.MediaTypes._
import spray.http.MediaType


object CollectionJsonDirective extends Directives {
  
  val `application/vnd.collection+json` = register(
    MediaType.custom(
      mainType = "application",
      subType = "vnd.collection+json",
      compressible = true,
      binary = true,
      fileExtensions = Seq.empty))
  
  private def getCollection[T : Convertable](href: URI)(service: CollectionJsonService[T]): CollectionJson = {
    val items = service.getItems
    
    Builder.newCollectionJson(href, items)
  }
  
  def route[T : Convertable](baseHref: URI)(service: CollectionJsonService[T]) =
    path(cleanPath(baseHref)) {
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getCollection[T](baseHref)(service)
          }
        }
      }
    }
  
  private def cleanPath(uri: URI) = if (uri.getPath().startsWith("/")) {
    uri.getPath().drop(1)
  } else {
    uri.getPath()
  }
}