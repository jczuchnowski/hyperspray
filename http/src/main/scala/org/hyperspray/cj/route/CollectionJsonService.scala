package org.hyperspray.cj.route

import scala.concurrent.Future

trait CollectionJsonService[Entity] {

  def idField: String = "id"
  
  def getAll: Seq[Entity]
  
  //TODO make id generic
  def getById(id: String): Option[Entity]
  
  // returns the new id
  def add(entity: Entity): String
}