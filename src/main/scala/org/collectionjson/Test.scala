package org.collectionjson

import org.collectionjson.macros._
import org.collectionjson.macros.Convertable._

case class Person(name: String, age: Int)

case class CollectionJson(params: Map[String, String])

trait Converter[T] {
  
  def convert(in: T): CollectionJson
}

object CollectionJsonConverter extends App {
  
  def convert(in: Person): CollectionJson = {
    val converter = materializeConvertable[Person]
    
    CollectionJson(Map())
  }
}