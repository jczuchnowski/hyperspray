package org.collectionjson

import java.net.URI
import org.collectionjson.model._
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros._

object Builder {

  def newCollectionJson[T: Convertable](href: URI, items: Seq[T]): CollectionJson = {
    
    val convItems = itemsWithData(href, items)
    
    val template = {
      val templateData = items.headOption map { item =>
        templateWithData(href, item)
      }
      
      templateData map { Template }
    }
    
    CollectionJson(
      Collection(href = href, items = convItems, template = template)
    )
  }
  
  def newCollectionJson[T : Convertable](href: URI, item: T): CollectionJson =
    newCollectionJson(href, Seq(item))
  
  private[this] def itemsWithData[T : Convertable](href: URI, items: Seq[T]) = items.map(_.asItem(href))
  
  //TODO could this be done without passing in any instance - only type ?
  // then the signature would be 'def templateWithData[T]: Seq[Data]
  private[this] def templateWithData[T : Convertable](href: URI, item: T): Seq[Data] = {
    val it = item.asItem(href)
    it.data.map(_.copy(value = None))
  }
  
}