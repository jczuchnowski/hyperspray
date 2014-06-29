package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Recoverable._
import org.collectionjson.model.Template
import scala.util.Try
import scala.util.Either

object Implicits2 {

  /**
   * Implicit case class builder from a Template .
   */
  implicit class EntityBuilder(tmpl: Template) {
    
    def asEntity[T : Recoverable]: Either[Issue, T] = {
      try {
        val params = tmpl.data.filter( d => d.value.isDefined ).map( d => (d.name, d.value.get))
        
        Right(implicitly[Recoverable[T]].fromParams(params))
      } catch {
        case e: NoSuchElementException =>
          Left(MissingFieldIssue())
      }
    }
  }
  
}