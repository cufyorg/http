/*
 *	Copyright 2021 Cufy and AgileSA
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
package org.cufy.http.client;

import org.jetbrains.annotations.Contract;

/**
 * A function that performs an operation on the parameter {@code T}.
 * <br>
 * Was made an abstract class to force it to not be a lambda.
 *
 * @param <T> the type of the parameter.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.13
 */
public abstract class Performer<T> {
	/**
	 * Perform the operation of this performer on the given {@code parameter}. The caller
	 * MUST call this only if sure that this performer will accept the parameter. The
	 * {@code null}-ability of the parameter depends on the specification of this
	 * performer.
	 * <br>
	 * Exception thrown by this performer must be caught and handled safely by the caller.
	 * (including {@link ClassCastException} from invoking this performer with a parameter
	 * that is not an instance of {@link T}.
	 * <br>
	 * Exception by a thread created by this performer is left for this performer to
	 * handle.
	 *
	 * @param parameter the parameter to perform the operation on.
	 * @throws Throwable if any exception occurred while performing the operation.
	 * @since 0.3.0 ~2021.12.13
	 */
	@Contract(mutates = "param")
	public abstract void perform(T parameter) throws Throwable;
}
