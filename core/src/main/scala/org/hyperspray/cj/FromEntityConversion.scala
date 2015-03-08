package org.hyperspray.cj

import java.net.URI
import org.hyperspray.macros._
import org.hyperspray.macros.Convertable._
import org.hyperspray.cj.model._
import org.hyperspray.cj.model.Properties._
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
    
    def asItem(uri: URI, idField: String): Item = {
      val params: Seq[(String, Any)] = implicitly[Convertable[T]].toParamSeq(t)
      
      val data = params.map( p => Data(name = p._1, value = Some(p._2)))
      
      val idVal = params.find(_._1 == idField).map(_._2.toString()).getOrElse("")
      
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

    def withTemplate(idField: String): CollectionJson = {

      // TODO could this be done without passing in any instance - only type ?
      // then the signature would be 'def templateWithData[T]: Seq[Data]
      def templateWithData(href: URI, item: Item, idField: String): Seq[Data] = {
        //remove the id field from the template
        val tmplIt = item.copy(data = item.data.filter(_.name != idField))
        tmplIt.data map { _.copy(value = None) }
      }

      val template = {
        val templateData = cj.collection.items.headOption map { item =>
          templateWithData(cj.collection.href, item, idField)
        }
      
        templateData map { Template }
      }

      CollectionJson(cj.collection.copy(template = template))
    }
  }
}