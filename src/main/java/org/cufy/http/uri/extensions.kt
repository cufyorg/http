/*
 *	Copyright 2021-2022 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.http.uri

// Query

/** An alias for [Query.put]. */
@JvmName("putAt")
operator fun Query.set(name: String, value: String?): Unit =
    if (value === null) remove(name) else put(name, value)

// UserInfo

/** An alias for [UserInfo.put] and [UserInfo.remove]. */
@JvmName("putAt")
operator fun UserInfo.set(index: Int, value: String?): Unit =
    if (value === null) remove(index) else put(index, value)

/** An alias for [UserInfo.add]. */
@JvmName("leftShift")
operator fun UserInfo.plusAssign(value: String): Unit =
    add(value)
