package org.hyperspray.cj.route

import scala.concurrent.Future

/**
 * Set of method needed to fully implement the collection+json hypermedia type.
 */
trait CollectionJsonService[Entity, Id] {

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
  def deleteById(id: Id): Unit

  /**
   * Returns all entities.
   */
  def getAll: Seq[Entity]
  
  /**
   * Returns Entity based in provided Id.
   */
  def getById(id: Id): Option[Entity]
  
  /**
   * Adds a new Entity to the underlying collection and returns its new Id.
   */
  def add(entity: Entity): Id
}