package org.hyperspray.cj.model

object Properties {

  /**
   *  Rel property
   */
  trait Rel {
    def rel: String
  }

  case object Profile extends Rel {
    val rel = "profile"
  }
  
  case object Search extends Rel {
    val rel = "search"
  }
  
  
  /**
   *  Render property
   */
  sealed trait Render {
    def rend: String
  }
  
  //named Lnk to avoid name collision
  case object Lnk extends Render {
    val rend = "link"
  }
  
  case object Image extends Render {
    val rend = "image"
  }
}