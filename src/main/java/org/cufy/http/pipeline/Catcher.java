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
import org.jetbrains.annotations.Nullable;

/**
 * A catcher is another functional interface form of the interface {@link Next} that only
 * executes when there is an error.
 *
 * @param <T> the type of the parameter the pipe accepts.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.09
 */
@FunctionalInterface
public interface Catcher<T> extends Next<T> {
	/**
	 * Has been overridden to redirect to do nothing. Do not invoke it directly nor
	 * override it.
	 * <br>
	 * {@inheritDoc}
	 *
	 * @throws Throwable {@inheritDoc}
	 */
	@Override
	@Deprecated
	default void invoke() {
	}

	/**
	 * Has been overridden to redirect to {@link #handle(Throwable)} if there is an error.
	 * Do not invoke it directly nor override it. Use/override {@link #handle(Throwable)}
	 * instead.
	 * <br>
	 * {@inheritDoc}
	 *
	 * @param error {@inheritDoc}
	 * @throws Throwable {@inheritDoc}
	 */
	@Override
	@Deprecated
	default void invoke(@Nullable Throwable error) {
		if (error != null)
			this.handle(error);
	}

	/**
	 * Handle the given {@code error}.
	 *
	 * @param error the error to be handled.
	 * @throws NullPointerException if the given {@code error} is null.
	 * @since 1.0.0 ~2022.01.09
	 */
	void handle(@NotNull Throwable error);
}
