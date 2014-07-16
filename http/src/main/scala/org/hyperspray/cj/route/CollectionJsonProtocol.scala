package org.hyperspray.cj.route

import java.net.URI
import org.hyperspray.cj.model._
import spray.json._

object CollectionJsonProtocol extends DefaultJsonProtocol {

  implicit val uriFormat = new JsonFormat[URI] {
    def write(o:URI) = JsString(o.toString())
    def read(value:JsValue) = new URI(value.toString())
  }
  
  //TODO handle more cases
  implicit val anyFormat = new JsonFormat[Any] {
    def write(o:Any) = JsString(o.toString())
    def read(value:JsValue) = value match  {
      case v: JsString => v.convertTo[String]
      case v: JsNumber => v.convertTo[Int]
      case v: JsBoolean => v.convertTo[Boolean]
    }
  }
  
  implicit val queryDataFormat = jsonFormat2(QueryData)
  implicit val queryFormat = jsonFormat5(Query) 
  implicit val linkFormat = jsonFormat5(Link)
  implicit val dataFormat = jsonFormat3(Data)
  implicit val itemFormat = jsonFormat3(Item)
  implicit val errorFormat = jsonFormat3(Error)
  implicit val templateFormat = jsonFormat1(Template)
  implicit val collectionFormat = jsonFormat7(Collection)
  implicit val collectionJsonFormat = jsonFormat1(CollectionJson)
  
  implicit val addEntityCommand = jsonFormat1(Commands.AddEntityCommand)
}