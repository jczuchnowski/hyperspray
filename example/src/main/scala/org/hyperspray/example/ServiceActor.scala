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
import org.collectionjson.FromEntityConversion._
import org.collectionjson.macros.Convertable
import org.collectionjson.macros.Recoverable
import org.hyperspray.collectionjson.route.CollectionJsonRoute
import org.hyperspray.collectionjson.route.CollectionJsonService

class ServiceActor() extends Actor with ActorLogging with HttpService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  implicit def executionContext: ExecutionContext = context.dispatcher

  implicit def myExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
    case e: Throwable => ctx => {
      log.error("Request {} could not be handled normally", ctx.request)
      log.error(e, "Server error")
      ctx.complete(InternalServerError, "Error - we've messed up something!!!")
    }
  }
  
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
  
  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = 
    runRoute(
      CollectionJsonRoute(baseHref, service)
    )
  
  implicit val timeout: Timeout = Timeout(2.seconds)
  
}