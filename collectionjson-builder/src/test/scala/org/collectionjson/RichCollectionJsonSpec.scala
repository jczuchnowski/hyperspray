package org.collectionjson

import java.net.URI
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros._
import org.collectionjson.model._
import org.collectionjson.model.Properties._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class RichCollectionJsonSpec extends FlatSpec with Matchers {

  case class TestItem(id: String, name: String, age: Int)
  
  val baseHref = "http://localhost:8080/test-items"
  val profileLink = "http://example.org/profiles/vcard"
  
  "A RichCollectionJson" should "add new profile link" in {
    
    val cj = Builder.newCollectionJson[TestItem](new URI(baseHref), Seq.empty).
      withProfileLink(new URI(profileLink))
    
    cj.collection.links shouldBe Seq(Link(href = new URI(profileLink), rel = Profile.rel))
  }
}