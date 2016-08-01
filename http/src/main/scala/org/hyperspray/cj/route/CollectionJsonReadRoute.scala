package org.hyperspray.cj.route

import java.net.URI

import org.hyperspray.cj.Builder
import org.hyperspray.macros._
import org.hyperspray.macros.Convertable._
import org.hyperspray.cj.model._
import org.hyperspray.cj.route.CollectionJsonProtocol._

import scala.concurrent.{ExecutionContext, Future}

import spray.httpx.SprayJsonSupport._
import spray.routing.Directives._


abstract class CollectionJsonReadRoute[Ent : Convertable, I](basePath: String)(implicit val executionContext: ExecutionContext) { 
  
  self: CollectionJsonEntityIdProvider[I] =>
  
  import CollectionJsonRoute._
  import org.hyperspray.cj.Builder._
    
  lazy val route =
    schemeName { sName =>
      hostName { hName =>
        lazy val baseHref = new URI(s"$sName://$hName/$basePath")
        pathPrefix(basePath) {
          respondWithMediaType(`application/vnd.collection+json`) {
            (get & pathEnd) {
              complete {
                getCollection(baseHref)
              }
            } ~
            (get & path("search") & parameterMap) { params =>
              complete {
                search(baseHref, params)
              }
            } ~
            (get & path(Segment)) { id =>
              complete {
                getEntity(baseHref, idFromString(id))
              }
            }
          }
        }
      }
    }

  private[this] def search(baseHref: URI, criteria: Map[String, String]): Future[CollectionJson] =    
    find(criteria) map { items => 
      val builder = new Builder(baseHref, items, idField)
      builder.newCollectionJson
    }

  private[this] def getEntity(baseHref: URI, id: I): Future[Option[CollectionJson]] = 
    getById(id) map { entity => 
      entity.map { it => new Builder(baseHref, Seq(it), idField).newCollectionJson }
    }

  private[this] def getCollection(baseHref: URI): Future[CollectionJson] =
    getAll map { items => {
      val builder = new Builder(baseHref, items, idField)
      builder.newCollectionJson
    }}

  def find(criteria: Map[String, String]): Future[Seq[Ent]]
  def getById(id: I): Future[Option[Ent]]
  def getAll: Future[Seq[Ent]]

}
