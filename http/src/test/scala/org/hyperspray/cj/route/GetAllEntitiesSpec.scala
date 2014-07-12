package org.hyperspray.cj.route

import CollectionJsonRoute.`application/vnd.collection+json`
import java.net.URI

import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.macros.Convertable
import org.hyperspray.macros.Recoverable
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import spray.http.HttpEntity
import spray.http.HttpHeaders._
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest

class GetAllEntitiesSpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  val baseHref = new URI("http://0.0.0.0:8080/test-items")
    
  def route = (new CollectionJsonRoute[TestItem, Int](baseHref) with ExampleService).route
  
  "GET all" should "respond with application/vnd.collection+json media type" in {
    Get("/test-items") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 200 status code" in {
    Get("/test-items") ~> route ~> check {
      status shouldBe OK
    }    
  }
  
}