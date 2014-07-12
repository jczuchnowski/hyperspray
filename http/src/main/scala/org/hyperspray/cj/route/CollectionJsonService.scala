package org.hyperspray.cj.route

import scala.concurrent.Future

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