package org.hyperspray.collectionjson.route

import scala.concurrent.Future
import org.collectionjson.macros.Convertable

trait CollectionService[T] {

  def getItems: Seq[T]
}