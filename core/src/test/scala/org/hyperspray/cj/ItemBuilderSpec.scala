package org.hyperspray.cj

import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ItemBuilderSpec extends FlatSpec with Matchers {
  
  case class TestItem(id: String, name: String, age: Int)
  
  val baseURI = "http://localhost:8080/test-items/"
  val baseURInoSlash = "http://localhost:8080/test-items"
  
  val idField = "id"
  val item = TestItem("123", "qwe", 0).asItem(new URI(baseURI), idField)
  
  "An ItemBuilder" should "correctly map field names" in {
    item.data.map(_.name) should contain theSameElementsInOrderAs Seq("id", "name", "age")
  }
  
  it should "correctly map field values" in {
    item.data.map(_.value) should contain theSameElementsInOrderAs Seq(Some("123"), Some("qwe"), Some(0))    
  }
  
  it should "correctly map entity href with trailing slash" in {
    item.href shouldBe new URI(baseURI + "123")
  }
  
  it should "correctly map entity href without trailing slash" in {
    val itemNoSlash = TestItem("123", "qwe", 0).asItem(new URI(baseURInoSlash), idField)

    itemNoSlash.href shouldBe new URI(baseURInoSlash + "/123")
  }
}