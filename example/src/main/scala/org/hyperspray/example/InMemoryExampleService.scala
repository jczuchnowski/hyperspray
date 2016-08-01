package org.hyperspray.example

import org.hyperspray.cj.route.CollectionJsonEntityIdProvider
import org.hyperspray.example.model.{TestItem, TestItemData}
import scala.concurrent.Future

  
trait InMemoryExampleService extends CollectionJsonService[TestItem, TestItemData, Int] with CollectionJsonEntityIdProvider[Int] {
    
    var items = Seq(TestItem(1, "qwe", 10), TestItem(2, "asd", 20))
    
    override def idFromString(id: String) = id.toInt 
    
    def newId() = items.map(_.id).max + 1
    
    override def deleteById(id: Int): Future[Either[String, Unit]] = Future {
      items = items.filterNot(_.id == id)
      Right(())
    }

    override def getAll: Future[Seq[TestItem]] = Future(items)
    
    override def getById(id: Int): Future[Option[TestItem]] = Future {
      items.filter(_.id == id.toInt).headOption
    }
    
    override def add(item: TestItemData): Future[Either[String, Int]] = Future {
      val nId = newId()
      items = items :+ TestItem(nId, item.name, item.age)
      Right(nId)
    }
    
    override def find(criteria: Map[String, String]) = Future {
      val nameOpt = criteria.get("name")
      val ageOpt = criteria.get("age")
      
      items.filter { item =>
        nameOpt.map( n => item.name == n).getOrElse(true) &&
        ageOpt.map( a => item.age == a.toInt).getOrElse(true)
      }
    }
  }