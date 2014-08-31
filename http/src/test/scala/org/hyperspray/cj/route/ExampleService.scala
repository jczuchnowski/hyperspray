package org.hyperspray.cj.route

import scala.concurrent.Future

case class TestItem(id: Int, name: String, age: Int, active: Boolean)
  
trait ExampleService extends CollectionJsonService[TestItem, Int] {
    
    var items = Seq(TestItem(123, "qwe", 10, true))
    
    override def idFromString(id: String) = id.toInt 
    
    override def newId() = items.map(_.id).max + 1
    
    override def deleteById(id: Int): Future[Either[String, Unit]] = Future {
      items = items.filterNot(_.id == id)
      Right(())
    }
       
    override def getAll: Future[Seq[TestItem]] = Future(items)
    
    override def getById(id: Int): Future[Option[TestItem]] = Future {
      items.filter(_.id == id.toInt).headOption
    }
    
    override def add(item: TestItem): Future[Either[String, Int]] = Future {
      val nId = newId()
      items = items :+ item.copy(id = nId)
      Right(nId)
    }
    
    override def find(criteria: Map[String, String]) = Future {
      val nameOpt = criteria.get("name")
      val ageOpt = criteria.get("age")
      val activeOpt = criteria.get("active")
      
      items.filter { item =>
        nameOpt.map( n => item.name == n).getOrElse(true) &&
        ageOpt.map( a => item.age == a.toInt).getOrElse(true) &&
        activeOpt.map( act => item.active == act.toBoolean).getOrElse(false)
      }
    }
  }