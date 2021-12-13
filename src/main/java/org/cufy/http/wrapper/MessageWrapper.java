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

import org.cufy.http.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A cursor that delegates to a message.
 *
 * @param <M>    the type of the message.
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.09
 */
public interface MessageWrapper<M extends Message, Self extends MessageWrapper<M, Self>> {
	// Message

	/**
	 * Invoke the given {@code operator} with the current message as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.27
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self message(@NotNull Consumer<@NotNull M> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.message());
		return (Self) this;
	}

	/**
	 * Set the message to the given {@code message}.
	 *
	 * @param message the message to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code message} is null.
	 * @throws UnsupportedOperationException if the message cannot be changed.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self message(@NotNull M message);

	/**
	 * Return the wrapped message.
	 *
	 * @return the wrapped message.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(pure = true)
	M message();
}
