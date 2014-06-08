package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._

case class Person(name: String, age: Int)

object CollectionJsonConverter extends App {
  
  def mapify[T: Convertable](t: T) = implicitly[Convertable[T]].toParamSeq(t)
      
  val person = mapify[Person](Person("Jakub", 33))
  
  
  println(person)
}