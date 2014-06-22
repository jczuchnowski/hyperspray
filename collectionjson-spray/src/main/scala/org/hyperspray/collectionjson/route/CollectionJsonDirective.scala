package org.hyperspray.collectionjson.route

import java.net.URI
import org.collectionjson.Builder
import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._
import org.collectionjson.macros.Recoverable._
import org.collectionjson.model._
import org.hyperspray.collectionjson.route.CollectionJsonProtocol._
import spray.httpx.SprayJsonSupport._
import spray.routing.Directives
import spray.http.MediaTypes._
import spray.http.MediaType
import spray.http.HttpHeaders._
import spray.http.StatusCodes

object CollectionJsonDirective extends Directives {
  
  val `application/vnd.collection+json` = register(
    MediaType.custom(
      mainType = "application",
      subType = "vnd.collection+json",
      compressible = true,
      binary = true,
      fileExtensions = Seq.empty))
  
  private def getCollection[T : Convertable](href: URI, service: CollectionJsonService[T]): CollectionJson = {
    val items = service.getItems
    
    Builder.newCollectionJson(href, items)
  }
  
  private def getItem[T : Convertable](href: URI, service: CollectionJsonService[T], id: String): Option[CollectionJson] = {
    val item = service.getItem(id)
    
    item.map { Builder.newCollectionJson(href, _) }
  }
  
  /**
   * returns new ID
   */
  private def addItem[T : Recoverable](href: URI, service: CollectionJsonService[T], template: Template): String = {
    import org.collectionjson.Implicits2._
    
    val ent = template.asEntity[T]
    service.addItem(ent)
  }
  
  def route[T : Convertable : Recoverable](baseHref: URI, service: CollectionJsonService[T]) =
    path(cleanPath(baseHref) / Segment) { id =>
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getItem(baseHref, service, id)
          }
        }
      }
    } ~
    path(cleanPath(baseHref)) {
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getCollection(baseHref, service)
          }
        } ~
        post {
          entity(as[Template]) { tmpl =>
            
            val newId = addItem(baseHref, service, tmpl)
  
            respondWithHeader(`Location`(s"$baseHref/$newId")) {
              complete(StatusCodes.Created, "")
            }
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