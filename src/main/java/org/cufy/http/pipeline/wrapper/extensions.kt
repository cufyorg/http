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
package org.cufy.http.pipeline.wrapper

import org.cufy.http.pipeline.Interceptor
import org.cufy.http.pipeline.Next
import org.cufy.http.pipeline.Pipe

/** An alias for [NextWrapper.next] */
var <T, Self : NextWrapper<T, Self>> Self.next: Next<T>
    get() = next()
    set(v) = run { next(v) }

/** An alias for [PipeWrapper.pipe] */
var <T, Self : PipeWrapper<T, Self>> Self.pipe: Pipe<T>
    get() = pipe()
    set(v) = run { pipe(v) }
