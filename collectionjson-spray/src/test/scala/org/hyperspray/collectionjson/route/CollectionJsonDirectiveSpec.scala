package org.hyperspray.collectionjson.route

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import spray.testkit.ScalatestRouteTest
import spray.routing.HttpService
import org.collectionjson.macros.Recoverable
import org.collectionjson.macros.Convertable
import CollectionJsonDirective.`application/vnd.collection+json`
import java.net.URI
import spray.http.StatusCodes._
import org.collectionjson.Implicits._

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
  
  def route = CollectionJsonDirective.route[TestItem](baseHref, service)
  
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
  
  "base path POST" should "respond with application/vnd.collection+json media type" in {
    Post("/test-items") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 201 status code" in {
    Post("/test-items") ~> route ~> check {
      status shouldBe Created
    }    
  }
  
  it should "respond with 403 status code" in {
    Post("/test-items") ~> route ~> check {
      status shouldBe BadRequest
    }
  }
  
  it should "respond with a location header" in {
    Post("/test-items") ~> route ~> check {
      header("Location") shouldBe "http://0.0.0.0:8080/test-items/124"
    }
  }

}