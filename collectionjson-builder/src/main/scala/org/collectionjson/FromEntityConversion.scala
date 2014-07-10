package org.collectionjson

import java.net.URI
import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._
import org.collectionjson.model._
import org.collectionjson.model.Properties._
import scala.language.implicitConversions

object FromEntityConversion {

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
      
      // take the first parameter as an ID
      //TODO
      val idVal = params.headOption.map( p => p._2.toString()).getOrElse("")
      
      // make sure the URI ends with a slash
      val itemUri = if (uri.toString().endsWith("/")) {
        uri.resolve(idVal)
      } else {
        new URI(uri.toString() + "/").resolve(idVal)
      }
      
      Item(href = itemUri, data = data)
    }
  }
  
  implicit class RichCollectionJson(cj: CollectionJson) {
    
    def withProfileLink(href: URI): CollectionJson = {
      val newLinks = cj.collection.links :+ Link(href = href, rel = Profile.rel)
      
      CollectionJson(cj.collection.copy(links = newLinks))
    }
    
    def withSearchQuery(searchFields: List[String]): CollectionJson = {
      val uri = cj.collection.href
      
      val searchUri = (if (uri.toString().endsWith("/")) {
        uri
      } else {
        new URI(uri.toString() + "/")
      }).resolve("search")
      
      val data = searchFields map { QueryData(_, "")}
      
      val searchQuery = Query(href = searchUri, rel = "search", data = data)
      
      CollectionJson(cj.collection.copy(queries = cj.collection.queries :+ searchQuery))
    }
  }
}