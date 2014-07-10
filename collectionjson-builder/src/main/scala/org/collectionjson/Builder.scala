package org.collectionjson

import java.net.URI
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros._
import org.collectionjson.model._

object Builder {

  def newCollectionJson[T: Convertable](href: URI, items: Seq[T], idField: String, searchFields: List[String] = Nil): CollectionJson = {
    
    val convItems = itemsWithData(href, items)
    
    val template = {
      val templateData = items.headOption map { item =>
        templateWithData(href, item, idField)
      }
      
      templateData map { Template }
    }
    
    val queries = if (!searchFields.isEmpty) Seq(searchQuery(href, searchFields)) else Seq()
    
    CollectionJson(
      Collection(href = href, items = convItems, template = template, queries = queries)
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
  
  private[this] def searchQuery(href: URI, queryFor: List[String]): Query = {
    // make sure the URI ends with a slash
    val searchUri = (if (href.toString().endsWith("/")) {
      href
    } else {
      new URI(href.toString() + "/")
    }).resolve("search")

    val data = queryFor map { QueryData(_, "")}
    Query(href = searchUri, rel = "search", data = data)
  }
  
}