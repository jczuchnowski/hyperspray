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

import scala.concurrent.{ExecutionContext, Future}

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

abstract class CollectionJsonRoute[Ent : Convertable : Recoverable, I](basePath: String)(implicit val executionContext: ExecutionContext) extends Directives with LazyLogging { 
  
  self: CollectionJsonService[Ent, I] =>
  
  import CollectionJsonRoute._
    
  lazy val route =
    schemeName { sName =>
      hostName { hName =>
        lazy val baseHref = new URI(s"$sName://$hName/$basePath")
        path(basePath / "search") {
          respondWithMediaType(`application/vnd.collection+json`) {
            get {
              parameterMap { params =>
                complete {
                  search(baseHref, params)
                }
              }
            }
          }
        } ~
        path(basePath / Segment) { id =>
          respondWithMediaType(`application/vnd.collection+json`) {
            get {
              complete {
                getEntity(baseHref, idFromString(id))
              }
            } ~
            delete {
              complete {
                val resultFut = deleteEntity(idFromString(id))
            
                resultFut.map { maybeNewId =>
                  maybeNewId.fold(
                    error => {
                      logger.debug(error)
                      HttpResponse(StatusCodes.BadRequest, error)
                    },
                    _ =>
                      HttpResponse(status = StatusCodes.NoContent)
                  )
                }
              }
            }
          }
        } ~
        path(basePath) {
          respondWithMediaType(`application/vnd.collection+json`) {
            get {
              complete {
                getCollection(baseHref)
              }
            } ~
            post {
              entity(as[Commands.AddEntityCommand]) { cmd =>
                complete {
                  val newIdFut = addEntity(cmd.template)
            
                  newIdFut.map { maybeNewId =>
                    maybeNewId.fold(
                      error => {
                        logger.debug(error)
                        HttpResponse(StatusCodes.BadRequest, error)
                      },
                      id =>
                        HttpResponse(status = StatusCodes.Created, headers = `Location`(s"$baseHref/$id") :: Nil)
                    )                    
                  }
                }
              }
            }
          }
        }
      }
    }
  
  private[this] def deleteEntity(id: I): Future[Either[String, Unit]] = deleteById(id)
  
  private[this] def getCollection(baseHref: URI): Future[CollectionJson] = {
    val itemsFut = getAll
    
    itemsFut map { items => 
      Builder.newCollectionJson(baseHref, items, idField)
    }
  }
  
  private[this] def getEntity(baseHref: URI, id: I): Future[Option[CollectionJson]] = {
    val entityFut = getById(id)
    
    entityFut map { entity =>
      entity.map { it => Builder.newCollectionJson(baseHref, it, idField) }
    }
  }
  
  /**
   * Returns the Id if the new Entity or the Issue that prevented it from happening.
   */
  private[this] def addEntity(template: Template): Future[Either[String, I]] = {

    import org.hyperspray.cj.ToEntityConversion._
    
    //be sure there's no idField
    val secData = template.data.filterNot(_.name == idField)
    
    //add new idField
    val newData = secData :+ Data(name = idField, value = Some(newId))
    
    val newTemplate = template.copy(data = newData)
    
    val maybeEnt =
      newTemplate.asEntity[Ent]
    
    maybeEnt fold (
      (error) => Future(Left(error.error)),
      (entity) => {
        add(entity)
      }
    )
    
  }
  
  private[this] def search(baseHref: URI, criteria: Map[String, String]): Future[CollectionJson] = {
    val entitiesFut = find(criteria)
    
    entitiesFut map { ent => 
      Builder.newCollectionJson(baseHref, ent, idField)
    }
  }

}