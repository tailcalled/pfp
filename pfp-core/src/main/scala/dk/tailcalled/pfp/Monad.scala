package dk.tailcalled.pfp

import language._

trait Monad[M[_]] extends Monoidal[M] {
	def join[A](v: M[M[A]]): M[A]
	def pair[A, B](a: M[A], b: M[B]) = a.fmap(av => b.fmap(bv => (av, bv))(this))(this).join(this)
	def bind[A, B](f: A => M[B]): M[A] => M[B] = lift(f) andThen (join[B] _)
}
trait Comonad[W[_]] extends Functor[W] {
	def extract[A](a: W[A]): A
	def duplicate[A](a: W[A]): W[W[A]]
	def extend[A, B](f: W[A] => B): W[A] => W[B] = (duplicate[A] _) andThen lift(f)
}