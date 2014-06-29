package org.hyperspray.collectionjson.route

import CollectionJsonRoute.`application/vnd.collection+json`
import java.net.URI
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros.Convertable
import org.collectionjson.macros.Recoverable
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import spray.http.HttpEntity
import spray.http.HttpHeaders._
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest

class CollectionJsonDirectiveSpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  case class TestItem(id: String, name: String, age: Int)
  
  val baseHref = new URI("http://0.0.0.0:8080/test-items")
  
  def service = new CollectionJsonService[TestItem] {
    
    var items = Seq(TestItem("123", "qwe", 10))
    
    override def getItems: Seq[TestItem] = items
    
    override def getItem(id: String): Option[TestItem] = items.filter(_.id == id).headOption
    
    override def addItem(item: TestItem): String = {
      val newId = "124"
      items = items :+ item.copy(id = newId)
      newId
    }
  }
  
  def route = CollectionJsonRoute[TestItem](baseHref, service)
  
  "base path GET" should "respond with application/vnd.collection+json media type" in {
    Get("/test-items") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 200 status code" in {
    Get("/test-items") ~> route ~> check {
      status shouldBe OK
    }    
  }
  
  "item path GET" should "respond with application/vnd.collection+json media type" in {
    Get("/test-items/123") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 200 status code" in {
    Get("/test-items/123") ~> route ~> check {
      status shouldBe OK
    }    
  }
  
  it should "respond with 404 status code" in {
    Get("/test-items/111") ~> route ~> check {
      status shouldBe NotFound
    }    
  }
  
  val tmpl = HttpEntity(`application/json`, 
"""
{"template" : {
    "data" : [
        {"name" : "id", "value" : "124"},
        {"name" : "name", "value" : "Jakub"},
        {"name" : "age", "value" : 33}
    ]
}}
""")

  val badTmpl = HttpEntity(`application/json`, 
"""
{"template" : {
    "data" : [
        {"name" : "id", "value" : "124"},
        {"name" : "age", "value" : 33}
    ]
}}
""")
  
  "base path POST" should "respond with application/vnd.collection+json media type" ignore {
    Post("/test-items", tmpl) ~> route ~> check {
      //headers should contain (`Content-Type` -> `application/vnd.collection+json`)
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 201 status code" in {
    Post("/test-items", tmpl) ~> route ~> check {
      status shouldBe Created
    }
  }
  
  it should "respond with 403 status code" in {
    Post("/test-items", badTmpl) ~> route ~> check {
      status shouldBe BadRequest
    }
  }
  
  it should "respond with a location header" in {
    Post("/test-items", tmpl) ~> route ~> check {
      headers should contain (`Location`("http://0.0.0.0:8080/test-items/124"))
    }
  }

}