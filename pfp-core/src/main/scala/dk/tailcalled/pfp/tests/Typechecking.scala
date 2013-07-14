package dk.tailcalled.pfp.tests

import scala.language.experimental.macros

import scala.reflect.macros.{Context, TypecheckException}

object typeErrors {

	def apply(code: String) = macro applyImpl
	def applyImpl(c: Context)(code: c.Expr[String]): c.Expr[Option[String]] = {
		import c.universe._
		val Expr(Literal(Constant(codeVal: String))) = code
		try {
			c.typeCheck(c.parse("{" + codeVal + "}"))
			reify(None)
		}
		catch {
			case e: TypecheckException =>
				val msg = e.getMessage
				reify(Some(c.Expr[String](Literal(Constant(msg))).splice))
		}
	}

}