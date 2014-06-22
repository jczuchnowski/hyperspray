package org.hyperspray.collectionjson.route

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import spray.testkit.ScalatestRouteTest
import spray.routing.HttpService
import org.collectionjson.macros.Convertable
import CollectionJsonDirective.`application/vnd.collection+json`
import java.net.URI

class CollectionJsonDirectiveSpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  case class TestItem(id: String, name: String, age: Int)
  
  val baseHref = new URI("http://0.0.0.0:8080/test-items")
  val service = new CollectionJsonService[TestItem] {
    override def getItems: Seq[TestItem] = Seq(TestItem("123", "qwe", 10))
  }
  
  val route = CollectionJsonDirective.route(baseHref)(service)
  
  "GET method" should "respond with application/vnd.collection+json media type" in {
    Get("/test-items") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
}