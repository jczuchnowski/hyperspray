package org.hyperspray.cj

import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.hyperspray.cj.model._

object Builder {

  def newCollectionJson[T: Convertable](href: URI, items: Seq[T], idField: String): CollectionJson = {
    
    val convItems = itemsWithData(href, items, idField)
    
    val template = {
      val templateData = items.headOption map { item =>
        templateWithData(href, item, idField)
      }
      
      templateData map { Template }
    }
    
    val queries = {
      val queryData = items.headOption map { item =>
        queryWithData(item, idField)
      }
      
      val query = queryData map { q =>
        Seq(Query(href.resolve("search"), Properties.Search.rel, None, None, q)) 
      }
      
      query.getOrElse(Seq.empty)
    }

    CollectionJson(
      Collection(href = href, items = convItems, template = template, queries = queries)
    )
  }
  
  def newCollectionJson[T : Convertable](href: URI, item: T, idField: String): CollectionJson =
    newCollectionJson(href, Seq(item), idField)
  
  private[this] def itemsWithData[T : Convertable](href: URI, items: Seq[T], idField: String) = items.map(_.asItem(href, idField))
  
  // TODO could this be done without passing in any instance - only type ?
  // then the signature would be 'def templateWithData[T]: Seq[Data]
  private[this] def templateWithData[T : Convertable](href: URI, item: T, idField: String): Seq[Data] = {
    val it = item.asItem(href, idField)
    
    //remove the id field from the template
    val tmplIt = it.copy(data = it.data.filter(_.name != idField))
    tmplIt.data map { _.copy(value = None) }
  }
  
  private[this] def queryWithData[T : Convertable](item: T, idField: String): Seq[QueryData] = {
    val it = item.asItem(new URI("any"), idField)
    
    //remove the id field from the template
    val queryIt = it.copy(data = it.data.filter(_.name != idField))
    queryIt.data map { data => QueryData(data.name, "") }
  }
    
}