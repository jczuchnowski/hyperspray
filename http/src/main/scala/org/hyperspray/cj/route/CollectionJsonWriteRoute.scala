package org.hyperspray.cj.route

import com.typesafe.scalalogging.slf4j.LazyLogging

import java.net.URI

import org.hyperspray.cj.Builder
import org.hyperspray.cj.Issue
import org.hyperspray.macros._
import org.hyperspray.macros.Recoverable._
import org.hyperspray.cj.model._
import org.hyperspray.cj.route.CollectionJsonProtocol._

import scala.concurrent.{ExecutionContext, Future}

import spray.http._
import spray.http.HttpHeaders._
import spray.httpx.SprayJsonSupport._
import spray.routing.Directives._


abstract class CollectionJsonWriteRoute[Ent : Recoverable, I](basePath: String)(implicit val executionContext: ExecutionContext) extends LazyLogging { 
  
  self: CollectionJsonEntityIdProvider[I] =>
  
  import CollectionJsonRoute._
    
  lazy val route =
    schemeName { sName =>
      hostName { hName =>
        lazy val baseHref = new URI(s"$sName://$hName/$basePath")
        pathPrefix(basePath) {
          respondWithMediaType(`application/vnd.collection+json`) {
            (post & pathEnd & entity(as[Commands.AddEntityCommand])) { cmd =>
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
            } ~
            (delete & path(Segment)) { id =>
              complete {
                deleteById(idFromString(id)) map { maybeNewId =>
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
        }
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
    //val newData = secData :+ Data(name = idField, value = Some(newId))
    val newData = secData

    val newTemplate = template.copy(data = newData)
    
    val maybeEnt =
      newTemplate.asEntity[Ent]
    
    maybeEnt fold (
      (error) => Future(Left(error.error)),
      (entity) => add(entity)
    )
    
  }

  def add(entity: Ent): Future[Either[String, I]]
  def deleteById(id: I): Future[Either[String, Unit]]
  
}
