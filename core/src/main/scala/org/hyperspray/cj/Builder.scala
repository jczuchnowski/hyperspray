package org.hyperspray.cj

import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.hyperspray.cj.model._

object Builder {

}

class Builder[T : Convertable](val href: URI, val items: Seq[T], val idField: String) {
  
  def newCollectionJson: CollectionJson = {
    
    val convItems = itemsWithData(href, items, idField)
    
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
      Collection(href = href, items = convItems, queries = queries)
    )
  }

  private[this] def itemsWithData[T : Convertable](href: URI, items: Seq[T], idField: String) = items.map(_.asItem(href, idField))

  private[this] def queryWithData[T : Convertable](item: T, idField: String): Seq[QueryData] = {
    val it = item.asItem(new URI("any"), idField)
    
    //remove the id field from the template
    val queryIt = it.copy(data = it.data.filter(_.name != idField))
    queryIt.data map { data => QueryData(data.name, "") }
  }
}
