package dk.tailcalled.pfp

import language._

trait Free[T[_], F[+_]] {

	def inst[A]: T[F[A]]
	def functor: Functor[F]
	def lift[A](x: A): F[A]
	def lower[A](f: F[A])(implicit instance: T[A]): A

}
trait FreeOps {

	implicit def freeInstance[T[_], F[+_], A](implicit free: Free[T, F]): T[F[A]] = free.inst[A]
	implicit def freeFunctor[T[_], F[+_]](implicit free: Free[T, F]): Functor[F] = free.functor

}