package org.hyperspray.cj.route

import scala.concurrent.Future

trait CollectionJsonService[Entity, Id] {

  def idField: String = "id"
  
  def idFromString(id: String): Id
  
  def newId(): Id
  
  def getAll: Seq[Entity]
  
  def getById(id: Id): Option[Entity]
  
  // returns the new id
  def add(entity: Entity): Id
}