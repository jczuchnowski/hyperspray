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
  
  def getCollection[T : Convertable](href: String)(service: CollectionService[T]): CollectionJson = {
    val items = service.getItems
    
    val collJson = Builder.newCollectionJson(new URI(href), items)
    
    collJson
  }
  
  def route[T : Convertable](baseHref: String)(service: CollectionService[T]) = 
    path(baseHref) {
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getCollection[T](baseHref)(service)
          }
        }
      }
    }
}