import com.banno.license.Licenses._
import com.banno.license.Plugin.LicenseKeys._
import com.banno.license.Plugin._

name := "scala-enum"

version := "0.0.1"

licenseSettings

license := apache2("Copyright 2015 the original author or authors.")

val scalaV = "2.11.5"

scalaVersion := scalaV

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaV

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
