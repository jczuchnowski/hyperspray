package org.hyperspray.macros

import scala.language.experimental.macros
import scala.reflect.macros.Context

trait Recoverable[T] {
  def fromParams(p: Seq[Tuple2[String, Any]]): T
}

object Recoverable {
  implicit def materializeRecoverable[T]: Recoverable[T] = macro materializeRecoverableImpl[T]

  def materializeRecoverableImpl[T: c.WeakTypeTag](c: Context): c.Expr[Recoverable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    val companion = tpe.typeSymbol.companionSymbol

    val fields = tpe.declarations.collectFirst { 
      case m: MethodSymbol if m.isPrimaryConstructor => m 
    }.get.paramss.head

    val item = fields.map { field =>
      val name = field.name.toTermName
      val mapKey: String = name.decoded
      val returnType = tpe.declaration(name).typeSignature
      
      q"p.find( it => it._1 == $mapKey).map(_._2).get.asInstanceOf[$returnType]"
    }
    
    c.Expr[Recoverable[T]] { q"""
      new Recoverable[$tpe] {
        def fromParams(p: Seq[Tuple2[String, Any]]): $tpe = { $companion(..$item) }
      }
    """ }
  }
}