import sbt._
import Keys._

object PfpBuild extends Build {
	object Deps {
		object V {
			val Scala = "2.10.2"
			val Scalatest = "2.0.M5b"
		}
		val ScalaReflect = "org.scala-lang" % "scala-reflect" % V.Scala
		val Scalatest = "org.scalatest" %% "scalatest" % V.Scalatest % "test"
	}
	
	override def settings = super.settings ++ Seq(
		scalaVersion := Deps.V.Scala,
		scalacOptions ++= Seq("-deprecation", "-feature", "-Xfatal-warnings"),
		libraryDependencies ++= Seq(Deps.Scalatest, Deps.ScalaReflect)
	)

	lazy val root = Project("root", file(".")) aggregate core

	lazy val core = Project("pfp-core", file("pfp-core"))
}