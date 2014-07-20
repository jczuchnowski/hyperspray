package org.hyperspray.cj.route

import scala.concurrent.{ExecutionContext, Future}

/**
 * Set of method needed to fully implement the collection+json hypermedia type.
 */
trait CollectionJsonService[Entity, Id] {

  implicit def executionContext: ExecutionContext
  /**
   * Returns the name of the Entity's id field.
   */
  def idField: String = "id"
  
  /**
   * A function able to convert a String into an Id.
   */
  def idFromString(id: String): Id
  
  /**
   * Returns next unique Id.
   */
  def newId(): Id

  /**
   * Deletes Entity based in provided Id.
   */
  def deleteById(id: Id): Future[Unit]

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
  def add(entity: Entity): Future[Id]
}