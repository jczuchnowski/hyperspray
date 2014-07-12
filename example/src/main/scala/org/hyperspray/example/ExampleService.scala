package org.hyperspray.example

import org.hyperspray.cj.route.CollectionJsonService

case class TestItem(id: Int, name: String, age: Int)
  
class ExampleService extends CollectionJsonService[TestItem] {
    
    var items = Seq(TestItem(1, "qwe", 10), TestItem(2, "asd", 20))
    
    override def getAll: Seq[TestItem] = items
    
    override def getById(id: String): Option[TestItem] = items.filter(_.id == id.toInt).headOption
    
    override def add(item: TestItem): String = {
      val newId = items.map(_.id).max + 1
      items = items :+ item.copy(id = newId)
      newId.toString()
    }
  }