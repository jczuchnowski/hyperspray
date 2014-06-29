package org.hyperspray.example

import akka.actor.ActorSystem
import akka.actor.Props
import akka.io.IO
import spray.can.Http

class Boot extends App {
	
  implicit val system = ActorSystem("example-actor-system")
    
  // create and start our service actor
  val service = system.actorOf(Props(new ServiceActor()), "test-service")
    
  // create a new HttpServer using our handler tell it where to bind to
  IO(Http) ! Http.Bind(service, interface = "0.0.0.0", port = 8080)  
}