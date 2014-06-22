package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Recoverable._
import org.collectionjson.model.Template

object Implicits2 {

  /**
   * Implicit case class builder from a Template .
   */
  implicit class EntityBuilder(tmpl: Template) {
    
    def asEntity[T : Recoverable]: T = {
      val params = tmpl.data.map( d => (d.name, d.value))
      
      val item = implicitly[Recoverable[T]].fromParams(params)
      
      item
    }
  }
  
}