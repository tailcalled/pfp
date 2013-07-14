package dk.tailcalled

import language._

package object pfp extends MonoidOps with FunctorOps with FreeOps with CanonicalOps {

	implicit class ApplyOps[F[_], A](val u: F[A]) extends AnyVal {
		def map[B](f: A => B)(implicit functor: Functor[F]): F[B] = functor.lift(f)(u)
		def fmap[B](f: A => B)(implicit functor: Functor[F]): F[B] = functor.lift(f)(u)
		def flatMap[B](f: A => F[B])(implicit monad: Monad[F]): F[B] = monad.bind(f)(u)
		def bind[B](f: A => F[B])(implicit monad: Monad[F]): F[B] = monad.bind(f)(u)
		def extend[B](f: F[A] => B)(implicit comonad: Comonad[F]): F[B] = comonad.extend(f)(u)
		def pair[B](v: F[B])(implicit monoidal: Monoidal[F]): F[(A, B)] = monoidal.pair(u, v)
		def extract(implicit comonad: Comonad[F]): A = comonad.extract(u)
		def duplicate(implicit comonad: Comonad[F]): F[F[A]] = comonad.duplicate(u)
	}
	implicit class Apply2Ops[F[_], A](val u: F[F[A]]) extends AnyVal {
		def join(implicit monad: Monad[F]): F[A] = monad.join(u)
	}
	implicit class IdOps[A](val u: A) extends AnyVal {
		def point[F[_]](implicit monoidal: Monoidal[F]): F[A] = monoidal.point(u)
		def ++(v: A)(implicit monoid: Monoid[A]) = monoid.append(u, v)
		def fix[B](implicit canonical: Canonical[A, B]) = canonical(u)
	}
	implicit class BooleanOps(val u: Boolean) extends AnyVal {
		def guard[A](a: A)(implicit monoid: Monoid[A]) = if (u) a else unit[A]
	}
	implicit class Func1Ops[A, B](val f: A => B) extends AnyVal {
		def lift[F[_]](implicit functor: Functor[F]): F[A] => F[B] = functor.lift(f)
		def on[F[_]](a: F[A])(implicit functor: Functor[F]): F[B] = a.map(f)
	}
	implicit class Func2Ops[A, B, C](val f: (A, B) => C) extends AnyVal {
		def on[F[_]](a: F[A], b: F[B])(implicit monoidal: Monoidal[F]): F[C] = (a pair b).map(f.tupled)
	}

}