/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package enum

import org.scalatest.{Matchers, FlatSpec}

class InstanceEnumOfTest extends FlatSpec with Matchers {

  it should "provide values for instance enum" in {
    val DistanceFromWarsaw = DistanceFrom("Warsaw", Coordinate(52.232938, 21.0611941))

    DistanceFromWarsaw.ToBerlin.inKm shouldEqual 519
    DistanceFromWarsaw.ToNewYork.inKm shouldEqual 6856

    DistanceFromWarsaw.values.map(_.inKm) shouldEqual List(519, 6856)
  }

}

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

case class Coordinate(latitude: Double, longitude: Double)

object Coordinate {
  val EARTH_RADIUS = 6371
  
  def distanceInKm(co1: Coordinate, co2: Coordinate): Double = {
    val (co1LatRad, co1LngRad) = (math.toRadians(co1.latitude), math.toRadians(co1.longitude))
    val (co2LatRad, co2LngRad) = (math.toRadians(co2.latitude), math.toRadians(co2.longitude))
    val dLat = co2LatRad - co1LatRad
    val dLong = co2LngRad - co1LngRad
    val a = math.pow(math.sin(dLat /2), 2) +
      math.cos(co1LatRad) * math.cos(co2LatRad) *
        math.pow(math.sin(dLong / 2), 2)
    val c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    EARTH_RADIUS * c
  }
}