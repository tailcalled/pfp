package dk.tailcalled.pfp

import language._
import dk.tailcalled.pfp.tests._
import org.scalatest.FunSpec

class InstancesSpecification extends FunSpec {

	describe ("vector") {
		it ("is a free monoid") {
			assert(/* there are no */typeErrors("""implicitly[Free[Monoid, Vector]]"""))
		}
	}

}