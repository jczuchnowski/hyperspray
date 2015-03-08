package org.hyperspray.cj

import java.net.URI

import org.hyperspray.macros._
import org.hyperspray.cj.model._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.language.postfixOps

class BuilderSpec extends FlatSpec with Matchers {

  case class TestItem(id: String, name: String, age: Int)
  
  val href = new URI("http://0.0.0.0:8080/items") //any valid URI

  val testItems = Seq(
    TestItem("id1", "name1", 11), 
    TestItem("id2", "name2", 22), 
    TestItem("id3", "name3", 33))
  
  "A CollectionJson Builder" should "create minimal CollectionJson" in {    
    val coll = new Builder(href, Seq.empty[Unit], "id").newCollectionJson
    
    assert( coll == CollectionJson(Collection("1.0", href, Seq.empty, Seq.empty, Seq.empty, None, None)))
  }
  
  it should "create CollectionJson with Items" in {
    val coll = new Builder(href, testItems, "id").newCollectionJson

    val items = coll.collection.items
    
    assert( items.size == testItems.size )
    
    // converted items have the same attributes
    items.zip(testItems).foreach { case (item, tstItem) => 
      assert( item.data(0).value == Some(tstItem.id))
      assert( item.data(1).value == Some(tstItem.name))
      assert( item.data(2).value == Some(tstItem.age))
    }
  }
  
  it should "create CollectionJson with Query" in {
    val coll = new Builder(href, testItems, "id").newCollectionJson

    val queries = coll.collection.queries
    
    val expected =
      Seq(
        Query(
          href.resolve("search"), 
          "search", 
          None, 
          None, 
          Seq(
            QueryData("name", ""), 
            QueryData("age", "")
          )
        )
      )
    
    queries should be (expected)    
  }

}