package org.hyperspray.cj.route

import java.net.URI

import org.hyperspray.cj.Builder
import org.hyperspray.cj.Issue
import org.hyperspray.macros._
import org.hyperspray.macros.Convertable._
import org.hyperspray.macros.Recoverable._
import org.hyperspray.cj.model._
import org.hyperspray.cj.route.CollectionJsonProtocol._

import spray.http._
import spray.http.HttpHeaders._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.routing.Directives
import spray.routing.Route

object CollectionJsonRoute extends Directives {
  
  val `application/vnd.collection+json` = register(
    MediaType.custom(
      mainType = "application",
      subType = "vnd.collection+json",
      compressible = true,
      binary = true,
      fileExtensions = Seq.empty))
  
  private def getCollection[T : Convertable](href: URI, service: CollectionJsonService[T]): CollectionJson = {
    val items = service.getItems
    
    Builder.newCollectionJson(href, items, service.idField)
  }
  
  private def getItem[T : Convertable](href: URI, service: CollectionJsonService[T], id: String): Option[CollectionJson] = {
    val item = service.getItem(id)
    
    item.map { it => Builder.newCollectionJson(href, it, service.idField) }
  }
  
  /**
   * returns new ID
   */
  private def addItem[T : Recoverable](href: URI, service: CollectionJsonService[T], template: Template): Either[Issue, String] = {

    import org.hyperspray.cj.ToEntityConversion._
    
    val maybeEnt = template.asEntity[T]
    
    maybeEnt fold (
      (error) => Left(error),
      (entity) =>
        Right(service.addItem(entity))
    )
    
  }
  
  def apply[T : Convertable : Recoverable](baseHref: URI, service: CollectionJsonService[T]): Route =
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
          entity(as[Commands.AddItemCommand]) { cmd =>
            
            val tryNewId = addItem(baseHref, service, cmd.template)
            
            tryNewId match {
              case Right(newId) => 
                respondWithHeader(`Location`(s"$baseHref/$newId")) {
                  complete(StatusCodes.Created, "")
                }
              case Left(issue) =>
                complete(StatusCodes.BadRequest, issue.error)
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