/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.pipeline;

import org.jetbrains.annotations.NotNull;

/**
 * An interceptor is another functional interface form of the interface {@link Pipe} that
 * always invokes the next function after its invocation. So, the lambda implementation
 * doesn't have to call the next function.
 *
 * @param <T> the type of the parameter of the pipe.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface Interceptor<T> extends Pipe<T> {
	@Override
	default void invoke(@NotNull T parameter, @NotNull Next<T> next) throws Throwable {
		this.invoke(parameter);
		next.invoke();
	}

	/**
	 * Invoke the code of this interceptor.
	 *
	 * @param parameter the parameter to invoke this pipe with.
	 * @throws NullPointerException if the given {@code next} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	void invoke(@NotNull T parameter) throws Throwable;
}
