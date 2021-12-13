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
package org.cufy.http.wrapper;

import org.cufy.http.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A cursor that delegates to a response.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.09
 */
public interface ResponseWrapper<Self extends ResponseWrapper<Self>> extends MessageWrapper<Response, Self> {
	// override

	@Override
	@NotNull
	default Response message() {
		return this.response();
	}

	@Override
	@NotNull
	default Self message(@NotNull Response message) {
		return this.response(message);
	}

	// Response

	/**
	 * Invoke the given {@code operator} with the current response as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self response(@NotNull Consumer<@NotNull Response> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.response());
		return (Self) this;
	}

	/**
	 * Set the response to the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self response(@NotNull Response response);

	/**
	 * Return the response.
	 *
	 * @return the response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	Response response();
}
