package org.hyperspray.cj.route

import scala.concurrent.{ExecutionContext, Future}


/**
 * Set of method needed to fully implement the collection+json hypermedia type.
 */
trait CollectionJsonService[Entity, Id] {

  implicit def executionContext: ExecutionContext
  
  /**
   * Deletes Entity based in provided Id.
   */
  def deleteById(id: Id): Future[Either[String, Unit]]

  /**
   * Returns all entities.
   */
  def getAll: Future[Seq[Entity]]
  
  /**
   * Returns Entity based in provided Id.
   */
  def getById(id: Id): Future[Option[Entity]]
  
  /**
   * Adds a new Entity to the underlying collection and returns its new Id.
   */
  def add(entity: Entity): Future[Either[String, Id]]
  
  def find(criteria: Map[String, String]): Future[Seq[Entity]]
}