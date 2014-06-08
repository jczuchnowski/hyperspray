package org.collectionjson.macros

import scala.language.experimental.macros
import scala.reflect.macros.Context

import scala.reflect.macros.Context

trait Convertable[T] {
  def toMap(t: T): Map[String, Any]
}

object Convertable {
  implicit def materializeConvertable[T]: Convertable[T] = macro materializeConvertableImpl[T]

  def materializeConvertableImpl[T: c.WeakTypeTag](c: Context): c.Expr[Convertable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    c.Expr[Convertable[T]] { q"""
      new Convertable[$tpe] {
        def toMap(t: $tpe) = Map[String, Any]()
      }
    """ }
  }
}