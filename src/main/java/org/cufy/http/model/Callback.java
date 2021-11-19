/*
 *	Copyright 2021 Cufy
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
package org.cufy.http.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A function that gets called when an event occurs. The callback will be invoked in an
 * unspecified thread, unspecified locks (threading), and unspecified order (to the other
 * callbacks). So, the callback must not be designed to depend on any of these factors.
 * <br>
 * But, the callback can depend on the parameter type of it. The caller (or its unit) and
 * the callback must specify a standard between them about what action expect what
 * parameter typ (we are not in JS here).
 * <br>
 * Also, the parameter can depend on the type of its caller. The one who registered
 * the callback is the responsible for making sure that the callback is registered on the
 * caller it is expecting.
 *
 * @param <T> the type of the expected caller.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
@FunctionalInterface
public interface Callback<T> {
	/**
	 * Return a new callback that calls the given {@code callbacks} in order when it gets
	 * called.
	 *
	 * @param callbacks the callbacks to be combined.
	 * @param <T>       the type of the accepted parameter.
	 * @return a new callback from combining the given {@code callbacks}.
	 * @throws NullPointerException if the given {@code callbacks} is null.
	 * @since 0.2.8 ~2021.08.27
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	static <T> Callback<T> callback(@Nullable Callback<? super T> @NotNull ... callbacks) {
		Objects.requireNonNull(callbacks, "callbacks");
		return (client, parameter) -> {
			for (Callback<? super T> callback : callbacks)
				if (callback != null)
					callback.call(client, parameter);
		};
	}

	/**
	 * Call this callback with the given {@code parameter}. The caller MUST call this only
	 * if sure that this callback will accept the parameter. The {@code null}-ability of
	 * the parameter depends on the action specification of the standard this callback is
	 * following.
	 * <br>
	 * Exception thrown by this callback must be caught and handled safely by the caller.
	 * (including {@link ClassCastException} from invoking this callback with a parameter
	 * that is not an instance of {@link T} or a caller that is not an instance of {@code
	 * C})
	 * <br>
	 * Exception by a thread created by this callback is left for this callback to
	 * handle.
	 *
	 * @param client    the client who called this callback.
	 * @param parameter the parameter to call this callback with.
	 * @throws Throwable if any expected or unexpected throwable got thrown.
	 * @since 0.0.1 ~2021.03.23
	 */
	void call(@NotNull Client client, @Nullable T parameter) throws Throwable;
}
