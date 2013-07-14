package dk.tailcalled.pfp

import language._
import dk.tailcalled.pfp.tests._
import org.scalatest.FunSpec

class ImplicitsSpecification extends FunSpec {

	describe ("canonical functions") {
		it ("exists as lifts of free structures") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type T[_]; type F[_]; type A
					implicit def free: Free[T, F]
					implicitly[Canonical[A, F[A]]]
				}
			"""))
		}
		it ("gets lifted over functors") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type F[_]; type A; type B
					implicit def functor: Functor[F]
					implicit def canonical: Canonical[A, B]
					implicitly[Canonical[F[A], F[B]]]
				}
			"""))
		}
		it ("inserts units for monoids") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type M; type I; type A
					implicit def monoid: Monoid[M]
					implicit def canonical: Canonical[I, A]
					implicitly[Canonical[I, (M, A)]]
					implicitly[Canonical[I, (A, M)]]
				}
			"""))
		}
	}

	describe ("free functors") {
		it ("implicitly has instances") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type T[_]; type F[_]; type A
					implicit def free: Free[T, F]
					implicitly[T[F[A]]]
				}
			"""))
		}
		it ("implicitly is a functor") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type T[_]; type F[_]
					implicit def free: Free[T, F]
					implicitly[Functor[F]]
				}
			"""))
		}
	}

	describe ("monoidal functors") {
		it ("sends monoids to monoids") {
			assert(/* there are no */typeErrors("""
				trait Postulate {
					type F[_]; type M
					implicit def functor: Monoidal[F]
					implicit def monoid: Monoid[M]
					implicitly[Monoid[F[M]]]
				}
			"""))
		}
	}

}