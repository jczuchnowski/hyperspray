package org.hyperspray.cj.mongo

import org.hyperspray.cj.route.CollectionJsonService

import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._

import scala.concurrent.{ExecutionContext, Future}


trait MongoRepository[Entity] extends CollectionJsonService[Entity, BSONObjectID] {

  implicit def format: BSONDocumentReader[Entity] with BSONDocumentWriter[Entity]

  implicit def executionContext: ExecutionContext

  def collection: BSONCollection

  //TODO provide macro based default implementation looking for @Id or _id field
  def getId(entity: Entity): BSONObjectID
  
  def add(e: Entity): Future[Either[String, BSONObjectID]] = {
    val result = collection.insert(e)
    
    for {
      err <- result
    } yield {
      if (err.ok) {
        Right(getId(e))
      } else {
        Left(err.errMsg.getOrElse("Unknown MongoDB error!"))
      }
    }
  }

  def getById(id: String): Future[Option[Entity]] =
    collection.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[Entity].headOption

  def deleteById(id: String): Future[Either[String, Unit]] = {
    val result = collection.remove(BSONDocument("_id" -> BSONObjectID(id)))
   
    for {
      err <- result
    } yield {
      if (err.ok) {
        Right(())
      } else {
        Left(err.errMsg.getOrElse("Unknown MongoDB error!"))
      }
    }

  }
    
  def getAll: Future[Seq[Entity]] =
    collection.find(BSONDocument.empty).cursor[Entity].collect[List]()
    
  //TODO default implementation
  def find(criteria: Map[String, String]): Future[Seq[Entity]]

}