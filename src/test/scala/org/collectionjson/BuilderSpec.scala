package org.collectionjson

import java.net.URI
import org.collectionjson.model._
import org.scalatest.FlatSpec

class BuilderSpec extends FlatSpec {

  case class TestItem(id: String, name: String, age: Int)
  
  val href = new URI("http://0.0.0.0:8080/items") //any valid URI

  val testItems = Seq(
    TestItem("id1", "name1", 11), 
    TestItem("id2", "name2", 22), 
    TestItem("id3", "name3", 33))
  
  "A CollectionJson Builder" should "create minimal CollectionJson" in {    
    val coll = Builder.newCollectionJson(href, Seq.empty)
    
    assert( coll == CollectionJson(Collection("1.0", href, Seq.empty, Seq.empty, Seq.empty, None, None)))
  }
  
  it should "create CollectionJson with Items" in {
    val coll = Builder.newCollectionJson(href, testItems)

    val items = coll.collection.items
    
    assert( items.size == testItems.size )
    
    // converted items have the same attributes
    items.zip(testItems).foreach { case (item, tstItem) => 
      assert( item.data(0).value == Some(tstItem.id))
      assert( item.data(1).value == Some(tstItem.name))
      assert( item.data(2).value == Some(tstItem.age))
    }
  }

}