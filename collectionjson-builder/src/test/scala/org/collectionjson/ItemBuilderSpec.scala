package org.collectionjson

import java.net.URI
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ItemBuilderSpec extends FlatSpec with Matchers {
  
  case class TestItem(id: String, name: String, age: Int)
  
  val baseURI = "http://localhost:8080/test-items/"
  val baseURInoSlash = "http://localhost:8080/test-items"
  
  val item = TestItem("123", "qwe", 0).asItem(new URI(baseURI))
  
  "An ItemBuilder" should "correctly map field names" in {
    item.data.map(_.name) should contain theSameElementsInOrderAs Seq("id", "name", "age")
  }
  
  it should "correctly map field values" in {
    item.data.map(_.value) should contain theSameElementsInOrderAs Seq(Some("123"), Some("qwe"), Some(0))    
  }
  
  it should "correctly map default entity href with trailing slash" in {
    item.href shouldBe new URI(baseURI + "123")
  }
  
  it should "correctly map default entity href without trailing slash" in {
    val itemNoSlash = TestItem("123", "qwe", 0).asItem(new URI(baseURInoSlash))

    itemNoSlash.href shouldBe new URI(baseURInoSlash + "/123")
  }
  
}