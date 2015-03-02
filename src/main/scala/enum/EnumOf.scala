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

import scala.reflect._

abstract class EnumOf[+Value: ClassTag] extends ObjectFieldsProvider {
  def values = {
    objectObjectFields.getOrElse(instanceObjectFields).collect {
      case v: Value => v
    }
  }

  def valueOfOpt(name: String): Option[Value] = values.find(_.toString == name)

  def valueOf(name: String): Value = valueOfOpt(name).getOrElse {
    throw new IllegalArgumentException(s"No enum value for name $name")
  }
}

trait ObjectFieldsProvider { self =>
  import runtime.universe._

  protected lazy val objectObjectFields: Option[List[Any]] = {
    val mirror = runtimeMirror(self.getClass.getClassLoader)
    val classSymbol = mirror.classSymbol(self.getClass)
    if (classSymbol.isModuleClass) {
      Some(sortedInnerModules(classSymbol).map(m => mirror.reflectModule(m.asModule).instance))
    } else {
      None
    }
  }

  protected def instanceObjectFields: List[Any] = {
    val mirror = runtimeMirror(self.getClass.getClassLoader)
    val classSymbol = mirror.classSymbol(self.getClass)
    sortedInnerModules(classSymbol).map(m => mirror.reflect(self).reflectModule(m.asModule).instance)
  }

  private def sortedInnerModules(classSymbol: ClassSymbol) = {
    classSymbol
      .toType
      .members
      .sorted // Doc: Symbols with the same owner appear in same order of their declarations
      .filter(_.isModule)
  }
}