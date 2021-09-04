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
package org.cufy.http.cache;

import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * An object representing a cached instance.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public interface Cache extends Cloneable, Serializable {
	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new cache instance to be a placeholder if a the user has not specified a
	 * cache.
	 *
	 * @return a new default cache.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Cache cache() {
		return new AbstractCache();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new copy of the given {@code cache}.
	 *
	 * @param cache the cache to copy.
	 * @return a copy of the given {@code cache}.
	 * @throws NullPointerException if the given {@code cache} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Cache cache(@NotNull Cache cache) {
		return new AbstractCache(cache);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new cache from the given components.
	 *
	 * @param request         the cached request.
	 * @param response        the cached response.
	 * @param creationInstant the creation instant.
	 * @return a new cache from the given components.
	 * @throws NullPointerException if the given {@code request} or {@code response} or
	 *                              {@code creationInstant} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	static Cache cache(@NotNull Request request, @NotNull Response response, @NotNull Instant creationInstant) {
		return new AbstractCache(request, response, creationInstant);
	}

	/**
	 * Replace the creation instant of this cache to be the result of invoking the given
	 * {@code operator} with the current creation instant of this cache. If the {@code
	 * operator} returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the creation instant of this cache cannot
	 *                                       be changed and the returned instant from the
	 *                                       given {@code operator} is different from the
	 *                                       current creation instant.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache creationInstant(@NotNull UnaryOperator<Instant> operator) {
		Objects.requireNonNull(operator, "operator");
		Instant i = this.getCreationInstant();
		Instant creationInstant = operator.apply(i);

		if (creationInstant != null && creationInstant != i)
			this.setCreationInstant(creationInstant);

		return this;
	}

	/**
	 * Invoke the given {@code operator} with {@code this} as the parameter and return the
	 * result returned from the operator.
	 *
	 * @param operator the operator to be invoked.
	 * @return the result from invoking the given {@code operator} or {@code this} if the
	 * 		given {@code operator} returned {@code null}.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract("_->new")
	default Cache map(UnaryOperator<Cache> operator) {
		Objects.requireNonNull(operator, "operator");
		Cache mapped = operator.apply(this);
		return mapped == null ? this : mapped;
	}

	/**
	 * Execute the given {@code consumer} with {@code this} as the parameter.
	 *
	 * @param consumer the consumer to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code consumer} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract("_->this")
	default Cache peek(Consumer<Cache> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		consumer.accept(this);
		return this;
	}

	/**
	 * Replace the request of this cache to be the result of invoking the given {@code
	 * operator} with the current request of this cache. If the {@code operator} returned
	 * null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the request of this cache cannot be
	 *                                       changed and the returned request from the
	 *                                       given {@code operator} is different from the
	 *                                       current request.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache request(@NotNull UnaryOperator<Request> operator) {
		Objects.requireNonNull(operator, "operator");
		Request r = this.getRequest();
		Request request = operator.apply(r);

		if (request != null && request != r)
			this.setRequest(request);

		return this;
	}

	/**
	 * Replace the response of this cache to be the result of invoking the given {@code
	 * operator} with the current response of this cache. If the {@code operator} returned
	 * null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the response of this cache cannot be
	 *                                       changed and the returned response from the
	 *                                       given {@code operator} is different from the
	 *                                       current response.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache response(@NotNull UnaryOperator<Response> operator) {
		Objects.requireNonNull(operator, "operator");
		Response r = this.getResponse();
		Response response = operator.apply(r);

		if (response != null && response != r)
			this.setResponse(response);

		return this;
	}

	/**
	 * Set the creation instant to be the given {@code creationInstant}.
	 *
	 * @param creationInstant the creation instant to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code creationInstant} is
	 *                                       null.
	 * @throws UnsupportedOperationException if the creation instant of this cache cannot
	 *                                       be changed.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache setCreationInstant(@NotNull Instant creationInstant) {
		throw new UnsupportedOperationException("setCreationInstant");
	}

	/**
	 * Set the cached request to be the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if the request of this cache cannot be
	 *                                       changed.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache setRequest(@NotNull Request request) {
		throw new UnsupportedOperationException("setRequest");
	}

	/**
	 * Set the cached response to be the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if the response of this cache cannot be
	 *                                       changed.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Cache setResponse(@NotNull Response response) {
		throw new UnsupportedOperationException("setResponse");
	}

	/**
	 * Capture this cache into a new object.
	 *
	 * @return a clone of this cache.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Cache clone();

	/**
	 * Two caches are equal when they are the same instance or have an equal {@link
	 * #getRequest()} and {@link #getResponse()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a cache and equals this.
	 * @since 0.2.11 ~2021.09.04
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a cache is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this cache.
	 * @since 0.2.11 ~2021.09.04
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this cache.
	 *
	 * @return a string representation of this Cache.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Return the creation instant of the cache.
	 *
	 * @return the creation instant.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	Instant getCreationInstant();

	/**
	 * Return the cached request.
	 *
	 * @return the cached request.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	Request getRequest();

	/**
	 * Return the cached response.
	 *
	 * @return the cached response.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	Response getResponse();
}
