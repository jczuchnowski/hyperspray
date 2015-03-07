package org.hyperspray.cj.route


trait CollectionJsonEntityIdProvider[Id] {
  
  /**
   * Returns the name of the Entity's id field.
   */
  val idField: String = "id"
  
  /**
   * A function able to convert a String into an Id.
   */
  def idFromString(id: String): Id
  
  /**
   * Returns next unique Id.
   */
  def newId(): Id
}