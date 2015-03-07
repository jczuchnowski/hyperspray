package org.hyperspray.cj.mongo

import org.hyperspray.cj.route.CollectionJsonEntityIdProvider
import reactivemongo.bson.BSONObjectID


trait MongoEntityIdProvider extends CollectionJsonEntityIdProvider[BSONObjectID] {

  override val idField: String = "_id"

  override def idFromString(id: String) = BSONObjectID(id) 
    
  override def newId() = BSONObjectID.generate
}