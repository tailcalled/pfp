package dk.tailcalled.pfp.modal

import dk.tailcalled.pfp._
import language._

trait Modal {

	type Next[+_]
	implicit def nextMonoidal: Monoidal[Next]

	def fix[A](f: Next[A] => A): A

	trait Signal[+A] {
		val now: A
		val later: Next[Signal[A]]
	}
	object Signal {
		def apply[A](_now: A, _later: Next[Signal[A]]) = new Signal[A] {
			val now = _now
			val later = _later
		}
	}
	sealed trait Eventually[+A]
	object Eventually {
		case class Now[+A](now: A) extends Eventually[A]
		case class Later[+A](later: Next[Eventually[A]]) extends Eventually[A]
	}

	sealed trait Events[+A]
	object Events {
		case class Now[+A](now: A, other: Events[A]) extends Events[A]
		case class Later[+A](later: Next[Events[A]]) extends Events[A]
	}

	implicit object SignalInstances extends Monoidal[Signal] with Comonad[Signal] {
		def lift[A, B](f: A => B): Signal[A] => Signal[B] = {
			var fLifted: Signal[A] => Signal[B] = null // ugly, but avoids extra allocations
			fLifted = (s: Signal[A]) => Signal[B](f(s.now), fLifted.on(s.later))
			fLifted
		}
		lazy val unit: Signal[Unit] =
			fix[Signal[Unit]](nextUnit => Signal((), nextUnit))
		def pair[A, B](a: Signal[A], b: Signal[B]) =
			Signal[(A, B)]((a.now, b.now), (pair[A, B] _).on(a.later, b.later))
		def extract[A](a: Signal[A]): A = a.now
		def duplicate[A](a: Signal[A]): Signal[Signal[A]] =
			Signal(a, a.later.map(_.duplicate))
	}
	def unfoldSteps[S](seed: S)(step: S => S): Signal[S] =
		fix[Signal[S]](res => Signal(seed, res.map(_.map(step))))
	def always[S](value: S): Signal[S] =
		fix[Signal[S]](res => Signal(value, res))
	def unfold[S](coalg: S => Next[S]): S => Signal[S] = {
		var rec: S => Signal[S] = null
		rec = s => Signal(s, coalg(s).map(x => rec(x)))
		rec
	}

}