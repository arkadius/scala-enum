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

class ObjectEnumOfTest extends FlatSpec with Matchers {
  import Color._
  import org.scalatest.OptionValues._

  it should "provide values for object enum" in {
    Color.values shouldEqual List(Red, Green, Blue, White, Black)

    Color.valueOfOpt("Blue").value shouldEqual Blue
    Color.valueOfOpt("NotExisiting").isEmpty shouldBe true
  }
}


sealed abstract class Color(red: Double, green: Double, blue: Double)

object Color extends EnumOf[Color] {
  case object Red   extends Color(1, 0, 0)
  case object Green extends Color(0, 1, 0)
  case object Blue  extends Color(0, 0, 1)
  case object White extends Color(0, 0, 0)
  case object Black extends Color(1, 1, 1)
}