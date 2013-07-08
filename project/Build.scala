import sbt._
import Keys._

object PfpBuild extends Build {
	object Deps {
		object V {
			val Scala = "2.10.2"
		}
	}
	
	override def settings = super.settings ++ Seq(
		scalaVersion := Deps.V.Scala,
		scalacOptions += "-feature"
	)

	lazy val root = Project("root", file(".")) aggregate core

	lazy val core = Project("pfp-core", file("pfp-core"))
}