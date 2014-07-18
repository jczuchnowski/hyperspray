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

class DeleteEntitySpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  val basePath = "test-items"
    
  def route = (new CollectionJsonRoute[TestItem, Int](basePath) with ExampleService).route
  
  "DELETE by id" should "respond with application/vnd.collection+json media type" ignore {
    Delete("/test-items/123") ~> route ~> check {
      mediaType shouldBe `application/vnd.collection+json`
    }
  }
  
  it should "respond with 204 status code when entity was found" in {
    Delete("/test-items/123") ~> route ~> check {
      status shouldBe NoContent
    }    
  }
  
  it should "respond with 204 status code when entity hasn't been found" in {
    Delete("/test-items/111") ~> route ~> check {
      status shouldBe NoContent
    }    
  }
  
}