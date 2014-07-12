package org.hyperspray.cj

import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros._
import org.hyperspray.cj.model._
import org.hyperspray.cj.model.Properties._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class RichCollectionJsonSpec extends FlatSpec with Matchers {

  case class TestItem(id: String, name: String, age: Int)
  
  val baseHref = "http://localhost:8080/test-items"
  val profileLink = "http://example.org/profiles/vcard"
  
  "A RichCollectionJson" should "add new profile link" in {
    
    val cj = Builder.newCollectionJson[TestItem](new URI(baseHref), Seq.empty, "id").
      withProfileLink(new URI(profileLink))
    
    cj.collection.links shouldBe Seq(Link(href = new URI(profileLink), rel = Profile.rel))
  }
  
  it should "add search query" in {
    val cj = Builder.newCollectionJson[TestItem](new URI(baseHref), Seq.empty, "id").
      withSearchQuery(List("name", "age"))
    
    cj.collection.queries shouldBe Seq(Query(href = new URI(baseHref + "/search"), rel = Search.rel, data = Seq(QueryData("name", ""), QueryData("age", ""))))    
  }
}