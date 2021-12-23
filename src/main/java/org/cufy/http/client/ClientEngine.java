/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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

import org.cufy.http.pipeline.Next;
import org.jetbrains.annotations.NotNull;

/**
 * A client engine is a core instance a client relies on when attempting to perform a
 * connection.
 *
 * @param <I> the type of the input parameter (the request).
 * @param <O> the type of the output parameter (the response).
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface ClientEngine<I, O> {
	/**
	 * Perform the connection with the given {@code input} and invoke the given {@code
	 * next} function when done.
	 *
	 * @param input the input instance.
	 * @param next  the next function.
	 * @throws NullPointerException          if the given {@code input} or {@code next} is
	 *                                       null.
	 * @throws UnsupportedOperationException if this engine does not support this
	 *                                       function.
	 * @since 0.3.0 ~2021.12.23
	 */
	void connect(@NotNull I input, @NotNull Next<O> next);
}
