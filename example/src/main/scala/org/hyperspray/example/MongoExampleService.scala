package org.hyperspray.example

import org.hyperspray.cj.mongo.{MongoRepository, MongoEntityIdProvider}
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Future


case class MongoTestItem(_id: BSONObjectID, name: String, age: Int)
  
trait MongoExampleService extends MongoRepository[MongoTestItem] with MongoEntityIdProvider {
            
    override def find(criteria: Map[String, String]) = {
      val nameOpt = criteria.get("name")
      val ageOpt = criteria.get("age")
      
      getAll
      // items.filter { item =>
      //   nameOpt.map( n => item.name == n).getOrElse(true) &&
      //   ageOpt.map( a => item.age == a.toInt).getOrElse(true)
      // }
    }
  }