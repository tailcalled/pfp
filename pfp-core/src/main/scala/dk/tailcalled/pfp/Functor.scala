package dk.tailcalled.pfp

import language._

trait Functor[F[+_]] {
	def lift[A, B](f: A => B): F[A] => F[B]
}
trait Monoidal[F[+_]] extends Functor[F] {
	def unit: F[Unit]
	def point[A](a: A): F[A] = unit.fmap(_ => a)(this)
	def pair[A, B](a: F[A], b: F[B]): F[(A, B)]
}
trait FunctorOps {
	def lazyPoint[A, F[+_]](a: => A)(implicit monoidal: Monoidal[F]): F[A] =
		monoidal.unit.fmap(_ => a)
}