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

class SearchEntitiesSpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  val basePath = "test-items"
    
  def route = (new CollectionJsonRoute[TestItem, Int](basePath) with ExampleService).route
  
  "GET by name" should "respond with application/vnd.collection+json media type" in {
    Get("/test-items/search?name=qwe") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 200 status code" in {
    Get("/test-items/search?name=qwe") ~> route ~> check {
      status shouldBe OK
    }    
  }
  
}