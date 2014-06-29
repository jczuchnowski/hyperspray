package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._
import org.collectionjson.model.CollectionJson
import org.collectionjson.model.Item
import org.collectionjson.FromEntityConversion._
import java.net.URI

case class Person(name: String, age: Int)

object CollectionJsonConverter extends App {
  
  //def mapify[T: Convertable](t: T) = implicitly[Convertable[T]].toParamSeq(t)
      
  //val person: Seq[(String, Any)] = Person("Jakub", 33)
  
  val person2: Item = Person("Jakub", 33).asItem(new URI("http://localhost/item/1"))
  //val collPerson: CollectionJson = Person("Maciek", 28)
  
  //println(person)
  println(person2)
}