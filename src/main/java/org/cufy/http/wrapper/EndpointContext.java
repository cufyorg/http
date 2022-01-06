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
package org.cufy.http.wrapper;

import org.cufy.http.Endpoint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An extended version of {@link EndpointWrapper}.
 *
 * @param <E>    the type of the endpoint.
 * @param <Self> the type of this wrapper.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.06
 */
public interface EndpointContext<E extends Endpoint, Self extends EndpointContext<E, Self>> extends EndpointWrapper<E, Self> {
	/**
	 * Invoke the given {@code getter} with this as the parameter and return the result of
	 * the invocation.
	 *
	 * @param getter the getter to be invoked.
	 * @param <T>    the type of the result.
	 * @return the result of invoking the given {@code getter}.
	 * @throws NullPointerException if the given {@code getter} is null.
	 * @since 1.0.0 ~2022.01.06
	 */
	@Contract(pure = true)
	default <T> T get(@NotNull Function<Self, T> getter) {
		Objects.requireNonNull(getter, "getter");
		return getter.apply((Self) this);
	}

	/**
	 * Invoke the given {@code setter} with this as the parameter.
	 *
	 * @param setter the setter to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code setter} is null.
	 * @since 1.0.0 ~2022.01.06
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self set(@NotNull Consumer<Self> setter) {
		Objects.requireNonNull(setter, "setter");
		setter.accept((Self) this);
		return (Self) this;
	}
}
