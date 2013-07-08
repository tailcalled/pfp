package dk.tailcalled.pfp

import language._

trait Semigroup[M] {
	def append(m: M, n: M): M
}
trait Monoid[M] extends Semigroup[M] {
	val unit: M
}
trait MonoidOps {
	def unit[A](implicit monoid: Monoid[A]) = monoid.unit

	implicit def monoidFromMonoidal[F[_], M]
		(implicit monoidal: Monoidal[F], monoid: Monoid[M]): Monoid[F[M]] = new MonoidFromMonoidal
	class MonoidFromMonoidal[F[_], M](implicit monoidal: Monoidal[F], monoid: Monoid[M]) extends Monoid[F[M]] {
		def append(m: F[M], n: F[M]) = (monoid.append _).on(m, n)
		val unit = monoid.unit.point[F]
	}
	implicit def vectorMonoid[A]: Monoid[Vector[A]] = new VectorMonoid[A]
	class VectorMonoid[A] extends Monoid[Vector[A]] {
		def append(m: Vector[A], n: Vector[A]) = m ++ n
		val unit = Vector()
	}
	implicit object UnitMonoid extends Monoid[Unit] {
		def append(m: Unit, n: Unit) = ()
		val unit = ()
	}
	implicit def pairMonoid[A, B](implicit a: Monoid[A], b: Monoid[B]): Monoid[(A, B)] = new PairMonoid
	class PairMonoid[A, B](implicit a: Monoid[A], b: Monoid[B]) extends Monoid[(A, B)] {
		def append(m: (A, B), n: (A, B)) = (m._1 ++ n._1, m._2 ++ n._2)
		val unit = (a.unit, b.unit)
	}

}