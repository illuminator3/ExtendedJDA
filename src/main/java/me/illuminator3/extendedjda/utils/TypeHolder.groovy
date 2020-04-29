/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 		Copyright 2020 illuminator3 aka Jonas Hardt

 		Licensed under the Apache License, Version 2.0 (the "License");
 		you may not use this file except in compliance with the License.
 		You may obtain a copy of the License at

 		   http://www.apache.org/licenses/LICENSE-2.0

 		Unless required by applicable law or agreed to in writing, software
 		distributed under the License is distributed on an "AS IS" BASIS,
 		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 		See the License for the specific language governing permissions and
 		limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package me.illuminator3.extendedjda.utils

class TypeHolder
{
    private final List<Class<?>> possibleTypes

    private Class<?> currentType
    private Object current

    TypeHolder(final Object initial, final List<Class<?>> possible)
    {
        this.possibleTypes = possible
        this.currentType = initial.class
        this.current = initial
    }

    void set(final Object object)
    {
        if (!possibleTypes.contains(object.class)) throw new IllegalArgumentException("The given object's type is not an instance of the possible ones")

        this.currentType = object.class
        this.current = object
    }

    Class<?> getType()
    {
        return this.currentType
    }

    Object get()
    {
        return this.current
    }

    @Deprecated
    <T> T getCasted()
    {
        return this.current as T
    }

    List<Class<?>> getPossibleTypes()
    {
        return this.possibleTypes
    }
}