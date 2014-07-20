package org.hyperspray.cj.route

import scala.concurrent.Future

case class TestItem(id: Int, name: String, age: Int, active: Boolean)
  
trait ExampleService extends CollectionJsonService[TestItem, Int] {
    
    var items = Seq(TestItem(123, "qwe", 10, true))
    
    override def idFromString(id: String) = id.toInt 
    
    override def newId() = items.map(_.id).max + 1
    
    override def deleteById(id: Int): Future[Unit] = Future {
      items = items.filterNot(_.id == id)
    }
       
    override def getAll: Future[Seq[TestItem]] = Future(items)
    
    override def getById(id: Int): Future[Option[TestItem]] = Future {
      items.filter(_.id == id.toInt).headOption
    }
    
    override def add(item: TestItem): Future[Int] = Future {
      val nId = newId()
      items = items :+ item.copy(id = nId)
      nId
    }
  }