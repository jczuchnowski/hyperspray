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
import org.hyperspray.macros.Convertable
import org.hyperspray.macros.Recoverable
import org.hyperspray.cj.route.CollectionJsonRoute
import org.hyperspray.cj.route.CollectionJsonService

class ServiceActor extends Actor with ActorLogging with HttpService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  implicit def executionContext: ExecutionContext = context.dispatcher
  
  case class TestItem(id: Int, name: String, age: Int)
  
  val baseHref = new URI("http://0.0.0.0:8080/test-items")
  
  def service = new CollectionJsonService[TestItem] {
    
    var items = Seq(TestItem(1, "qwe", 10), TestItem(2, "asd", 20))
    
    override def getAll: Seq[TestItem] = items
    
    override def getById(id: String): Option[TestItem] = items.filter(_.id == id.toInt).headOption
    
    override def add(item: TestItem): String = {
      val newId = items.map(_.id).max + 1
      items = items :+ item.copy(id = newId)
      newId.toString()
    }
  }
  
  def receive = runRoute(
    CollectionJsonRoute(baseHref, service)
  )
  
  implicit val timeout: Timeout = Timeout(2.seconds)
  
}