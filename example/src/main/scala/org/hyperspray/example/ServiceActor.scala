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

class ServiceActor extends Actor with ActorLogging with HttpService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  implicit def executionContext: ExecutionContext = context.dispatcher
  
  case class TestItem(id: String, name: String, age: Int)
  
  val baseHref = new URI("http://0.0.0.0:8080/test-items")
  
  def service = new CollectionJsonService[TestItem] {
    
    var items = Seq(TestItem("1", "qwe", 10), TestItem("2", "asd", 20))
    
    override def getItems: Seq[TestItem] = items
    
    override def getItem(id: String): Option[TestItem] = items.filter(_.id == id).headOption
    
    override def addItem(item: TestItem): String = {
      val newId = "124"
      items = items :+ item.copy(id = newId)
      newId
    }
  }
  
  def receive = runRoute(
    CollectionJsonRoute(baseHref, service)
  )
  
  implicit val timeout: Timeout = Timeout(2.seconds)
  
}