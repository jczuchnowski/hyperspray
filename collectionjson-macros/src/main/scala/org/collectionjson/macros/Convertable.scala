package org.collectionjson.macros

import scala.language.experimental.macros
import scala.reflect.macros.Context

trait Convertable[T] {
  def toParamSeq(t: T): Seq[Tuple2[String, Any]]
}

object Convertable {
  implicit def materializeConvertable[T]: Convertable[T] = macro materializeConvertableImpl[T]

  def materializeConvertableImpl[T: c.WeakTypeTag](c: Context): c.Expr[Convertable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    val fields = tpe.declarations.collectFirst { 
      case m: MethodSymbol if m.isPrimaryConstructor => m 
    }.get.paramss.head

    val paramSeq = fields.map { field =>
      val name = field.name.toTermName
      val mapKey: String = name.decoded
      q"$mapKey -> t.$name"
    }
    
    c.Expr[Convertable[T]] { q"""
      new Convertable[$tpe] {
        def toParamSeq(t: $tpe): Seq[Tuple2[String, Any]] = Seq(..$paramSeq)
      }
    """ }
  }
}