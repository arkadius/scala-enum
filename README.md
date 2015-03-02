scala-enum
==========

[![Build Status](https://travis-ci.org/arkadius/scala-enum.svg?branch=master)](https://travis-ci.org/arkadius/scala-enum)

This is simple 40-lines implementation of enums based on sealed classes (supporting pattern matching exhaustion checks).
It uses reflection so if you use it, you may need to add `libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaV` to your project.

# Enum for objects

## Definition

```scala
sealed abstract class Color(red: Double, green: Double, blue: Double)

object Color extends EnumOf[Color] {
  case object Red   extends Color(1, 0, 0)
  case object Green extends Color(0, 1, 0)
  case object Blue  extends Color(0, 0, 1)
  case object White extends Color(0, 0, 0)
  case object Black extends Color(1, 1, 1)
}
```

## Usage

```scala
Color.values shouldEqual List(Red, Green, Blue, White, Black)

Color.valueOfOpt("Blue").value shouldEqual Blue
Color.valueOfOpt("NotExisiting").isEmpty shouldBe true
```

# Enum for instances

## Definition

```scala
case class DistanceFrom(srcCity: String, srcCoordinates: Coordinate) extends EnumOf[DistanceBetween] {
  case object ToBerlin extends DistanceFromSrcCityTo("Berlin", Coordinate(52.5075419, 13.4251364))
  case object ToNewYork extends DistanceFromSrcCityTo("New York", Coordinate(40.7033127, -73.979681))

  abstract class DistanceFromSrcCityTo(val destCity: String, val destCoordinates: Coordinate) extends DistanceBetween {
    override def srcCoordinates: Coordinate = DistanceFrom.this.srcCoordinates
  }
}

sealed abstract class DistanceBetween {
  def srcCoordinates: Coordinate

  def destCity: String
  def destCoordinates: Coordinate

  def inKm: Int = Coordinate.distanceInKm(srcCoordinates, destCoordinates).toInt
}
```

## Usage

```scala
val DistanceFromWarsaw = DistanceFrom("Warsaw", Coordinate(52.232938, 21.0611941))

DistanceFromWarsaw.ToBerlin.inKm shouldEqual 519
DistanceFromWarsaw.ToNewYork.inKm shouldEqual 6856

DistanceFromWarsaw.values.map(_.inKm) shouldEqual List(519, 6856)
```

# License

The scala-enum is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).