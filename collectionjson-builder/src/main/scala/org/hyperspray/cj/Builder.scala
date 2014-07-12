package org.hyperspray.cj

import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.hyperspray.cj.model._

object Builder {

  def newCollectionJson[T: Convertable](href: URI, items: Seq[T], idField: String): CollectionJson = {
    
    val convItems = itemsWithData(href, items)
    
    val template = {
      val templateData = items.headOption map { item =>
        templateWithData(href, item, idField)
      }
      
      templateData map { Template }
    }
    
    CollectionJson(
      Collection(href = href, items = convItems, template = template)
    )
  }
  
  def newCollectionJson[T : Convertable](href: URI, item: T, idField: String): CollectionJson =
    newCollectionJson(href, Seq(item), idField)
  
  private[this] def itemsWithData[T : Convertable](href: URI, items: Seq[T]) = items.map(_.asItem(href))
  
  //TODO could this be done without passing in any instance - only type ?
  // then the signature would be 'def templateWithData[T]: Seq[Data]
  private[this] def templateWithData[T : Convertable](href: URI, item: T, idField: String): Seq[Data] = {
    val it = item asItem href
    
    //remove the id field from the template
    val tmplIt = it.copy(data = it.data.filter(_.name != idField))
   tmplIt.data map { _.copy(value = None) }
  }
    
}