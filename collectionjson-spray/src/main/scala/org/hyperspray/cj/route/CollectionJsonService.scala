package org.hyperspray.cj.route

import scala.concurrent.Future

trait CollectionJsonService[T] {

  def idField: String = "id"
  
  def getItems: Seq[T]
  
  //TODO make id generic
  def getItem(id: String): Option[T]
  
  // returns the new id
  def addItem(item: T): String
}