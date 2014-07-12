package org.hyperspray.cj.route

case class TestItem(id: Int, name: String, age: Int)
  
trait ExampleService extends CollectionJsonService[TestItem, Int] {
    
    var items = Seq(TestItem(123, "qwe", 10))
    
    override def idFromString(id: String) = id.toInt 
    
    override def newId() = items.map(_.id).max + 1
    
    override def getAll: Seq[TestItem] = items
    
    override def getById(id: Int): Option[TestItem] = items.filter(_.id == id.toInt).headOption
    
    override def add(item: TestItem): Int = {
      val nId = newId()
      items = items :+ item.copy(id = nId)
      nId
    }
  }