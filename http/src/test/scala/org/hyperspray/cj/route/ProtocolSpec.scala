package org.hyperspray.cj.route

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.hyperspray.cj.model.QueryData
import spray.json._
import DefaultJsonProtocol._

class ProtocolSpec extends FlatSpec with Matchers {

  import CollectionJsonProtocol._
    
  "AnyFormat" should "correctly serialize object to JSON" in {
    
    val number: Any = 2
    val jsonNumber = number.toJson
    jsonNumber shouldBe JsNumber(2)
    
    val str: Any = "2"
    val jsonStr = str.toJson
    jsonStr shouldBe JsString("2")
    
    val bool: Any = true
    val jsonBool = bool.toJson
    jsonBool shouldBe JsBoolean(true)
    
    val nul: Any = null
    val jsonNull = nul.toJson
    jsonNull shouldBe JsNull
    
  }
}