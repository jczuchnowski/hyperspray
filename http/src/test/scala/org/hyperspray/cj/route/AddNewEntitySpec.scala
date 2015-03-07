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

class AddNewEntitySpec extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {

  def actorRefFactory = system
  
  val basePath = "test-items"
    
  def route = (new CollectionJsonRoute[TestItem, Int](basePath) with CollectionJsonReadOps[TestItem, Int] with CollectionJsonWriteOps[TestItem, Int] with ExampleService {
    override def convertable = implicitly
    override def recoverable = implicitly
  }).route

  val tmpl = HttpEntity(`application/json`, 
"""
{"template" : {
    "data" : [
        {"name" : "name", "value" : "Jakub"},
        {"name" : "age", "value" : 33},
        {"name" : "active", "value" : true}
    ]
}}
""")

  //template with a missing field
  val badTmpl = HttpEntity(`application/json`, 
"""
{"template" : {
    "data" : [
        {"name" : "id", "value" : "124"},
        {"name" : "age", "value" : 33}
    ]
}}
""")
  
  "POST template" should "respond with application/vnd.collection+json media type" ignore {
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
      headers should contain (`Location`("http://example.com/test-items/124"))
    }
  }

}