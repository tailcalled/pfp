package dk.tailcalled.pfp

import language._

trait Canonical[A, B] {
	def apply(a: A): B
}
trait CanonicalOps {
	implicit def canonicalFunctor[A, B, F[_]](implicit functor: Functor[F], canonical: Canonical[A, B]): Canonical[F[A], F[B]] = new Canonical[F[A], F[B]] {
		def apply(a: F[A]): F[B] = a.map(canonical.apply _)
	}
}