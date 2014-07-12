package org.hyperspray.cj.route

import com.typesafe.scalalogging.slf4j.LazyLogging
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

object CollectionJsonRoute {
  
  val `application/vnd.collection+json` = register(
    MediaType.custom(
      mainType = "application",
      subType = "vnd.collection+json",
      compressible = true,
      binary = true,
      fileExtensions = Seq.empty))
}

abstract class CollectionJsonRoute[Ent : Convertable : Recoverable, I](val baseHref: URI) extends Directives with LazyLogging { 
  
  self: CollectionJsonService[Ent, I] =>
  
  import CollectionJsonRoute._
  
  private def getCollection(href: URI): CollectionJson = {
    val items = getAll
    
    Builder.newCollectionJson(href, items, idField)
  }
  
  private def getItem(href: URI, id: I): Option[CollectionJson] = {
    val item = getById(id)
    
    item.map { it => Builder.newCollectionJson(href, it, idField) }
  }
  
  /**
   * Returns the Id if the new Entity or the Issue that prevented it from happening.
   */
  private def addItem(href: URI, template: Template): Either[Issue, I] = {

    import org.hyperspray.cj.ToEntityConversion._
    
    //be sure there's no idField
    val secData = template.data.filterNot(_.name == idField)
    
    //add new idField
    val newData = secData :+ Data(name = idField, value = Some(newId))
    
    val newTemplate = template.copy(data = newData)
    
    val maybeEnt = newTemplate.asEntity[Ent]
    
    maybeEnt fold (
      (error) => Left(error),
      (entity) =>
        Right(add(entity))
    )
    
  }
  
  lazy val route =
    path(cleanPath(baseHref) / Segment) { id =>
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getItem(baseHref, idFromString(id))
          }
        }
      }
    } ~
    path(cleanPath(baseHref)) {
      respondWithMediaType(`application/vnd.collection+json`) {
        get {
          complete {
            getCollection(baseHref)
          }
        } ~
        post {
          entity(as[Commands.AddItemCommand]) { cmd =>
            
            val tryNewId = addItem(baseHref, cmd.template)
            
            tryNewId match {
              case Right(newId) => 
                respondWithHeader(`Location`(s"$baseHref/$newId")) {
                  complete(StatusCodes.Created, "")
                }
              case Left(issue) =>
                logger.debug(issue.error)
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