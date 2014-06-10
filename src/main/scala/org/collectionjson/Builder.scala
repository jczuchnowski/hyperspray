package org.collectionjson

import java.net.URI
import org.collectionjson.model._
import org.collectionjson.Implicits._

object Builder {

  def newCollectionJson[T: Convertable](href: URI, items: Seq[T]): CollectionJson = {
    val convItems = items.map(_.asItem(href))
    
    CollectionJson(
      Collection(href = href, items = convItems)
    )
  }
}