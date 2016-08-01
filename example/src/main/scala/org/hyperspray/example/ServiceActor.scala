package org.hyperspray.example

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.util.Timeout
import java.net.URI
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.util.LoggingContext
import org.hyperspray.cj.FromEntityConversion._
import org.hyperspray.cj.route.{CollectionJsonReadRoute, CollectionJsonWriteRoute}
import org.hyperspray.example.model.{TestItem, TestItemData}
import org.hyperspray.macros.{Convertable, Recoverable}

class ServiceActor extends Actor with ActorLogging with HttpService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  implicit def executionContext: ExecutionContext = context.dispatcher
    
  val basePath = "test-items"
  
  val readRoute = new CollectionJsonReadRoute[TestItem, Int](basePath) 
      with InMemoryExampleService
  
  val writeRoute = new CollectionJsonWriteRoute[TestItemData, Int](basePath) 
      with InMemoryExampleService

  def receive = runRoute(
    readRoute.route ~
    writeRoute.route
  )
  
  implicit val timeout: Timeout = Timeout(2.seconds)
  
}