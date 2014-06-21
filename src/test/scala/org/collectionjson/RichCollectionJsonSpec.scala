package org.collectionjson

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.collectionjson.Implicits._
import org.collectionjson.macros._
import org.collectionjson.model._
import org.collectionjson.model.CollectionJson._
import java.net.URI

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