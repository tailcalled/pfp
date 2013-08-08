package dk.tailcalled.pfp

import language._

trait Functor[F[+_]] {
	def lift[A, B](f: A => B): F[A] => F[B]
}
trait Monoidal[F[+_]] extends Functor[F] {
	val unit: F[Unit]
	def point[A](a: A): F[A] = unit.fmap(_ => a)(this)
	def pair[A, B](a: F[A], b: F[B]): F[(A, B)]
}
trait FunctorOps {
	
}