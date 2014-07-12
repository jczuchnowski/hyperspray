package org.hyperspray.cj

import java.net.URI
import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.hyperspray.macros.Convertable._
import org.hyperspray.cj.model.CollectionJson
import org.hyperspray.cj.model.Item

case class Person(name: String, age: Int)

object CollectionJsonConverter extends App {
  
  //def mapify[T: Convertable](t: T) = implicitly[Convertable[T]].toParamSeq(t)
      
  //val person: Seq[(String, Any)] = Person("Jakub", 33)
  
  val person2: Item = Person("Jakub", 33).asItem(new URI("http://localhost/item/1"))
  //val collPerson: CollectionJson = Person("Maciek", 28)
  
  //println(person)
  println(person2)
}