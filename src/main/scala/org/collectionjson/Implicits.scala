package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._
import org.collectionjson.model._
import scala.language.implicitConversions
import java.net.URI

object Implicits {

  /**
   * Implicit conversion from any case class to a Seq of paramName -> paramValue pairs.
   */
  implicit def itemToPairs[T: Convertable](t: T): Seq[(String, Any)] = implicitly[Convertable[T]].toParamSeq(t)

  /**
   * Implicit builder of a CollectionJSON Item.
   */
  implicit class ItemBuilder[T: Convertable](t: T) {
    
    def asItem(uri: URI): Item = {
      val params: Seq[(String, Any)] = implicitly[Convertable[T]].toParamSeq(t)
      val data = params.map( p => Data(name = p._1, value = Some(p._2)))
      Item(href = uri, data = data)
    }
  }
  
  
}