package org.hyperspray.collectionjson.route

import scala.concurrent.Future
import org.collectionjson.macros.Convertable

trait CollectionJsonService[T] {

  def getItems: Seq[T]
  
}