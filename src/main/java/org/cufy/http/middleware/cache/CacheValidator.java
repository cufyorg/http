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
package org.cufy.http.middleware.cache;

import org.cufy.http.cache.Cache;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

/**
 * A functional interface that accepts a request and a cache and tests if the cache is a
 * valid cache for the accepted request.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
@FunctionalInterface
public interface CacheValidator {
	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getAuthority() authority}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchAuthority() {
		return (request, cache) ->
				Objects.equals(
						request.getAuthority(),
						cache.getRequest().getAuthority()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getBody() body}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchBody() {
		return (request, cache) ->
				Objects.equals(
						request.getBody(),
						cache.getRequest().getBody()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getFragment() fragment}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchFragment() {
		return (request, cache) ->
				Objects.equals(
						request.getFragment(),
						cache.getRequest().getFragment()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getHeaders() headers}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchHeaders() {
		return (request, cache) ->
				Objects.equals(
						request.getHeaders(),
						cache.getRequest().getHeaders()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getHeaders() header} with the given {@code name}.
	 *
	 * @param name the name of the header to be compared.
	 * @return the predicate.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchHeaders(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		return (request, cache) ->
				Objects.equals(
						request.getHeaders().get(name),
						cache.getRequest().getHeaders().get(name)
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getHost() host}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchHost() {
		return (request, cache) ->
				Objects.equals(
						request.getHost(),
						cache.getRequest().getHost()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getHttpVersion() http version}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchHttpVersion() {
		return (request, cache) ->
				Objects.equals(
						request.getHttpVersion(),
						cache.getRequest().getHttpVersion()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getMethod() method}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchMethod() {
		return (request, cache) ->
				Objects.equals(
						request.getMethod(),
						cache.getRequest().getMethod()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getPath() path}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchPath() {
		return (request, cache) ->
				Objects.equals(
						request.getPath(),
						cache.getRequest().getPath()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getPort() port}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchPort() {
		return (request, cache) ->
				Objects.equals(
						request.getPort(),
						cache.getRequest().getPort()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getQuery() query}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchQuery() {
		return (request, cache) ->
				Objects.equals(
						request.getQuery(),
						cache.getRequest().getQuery()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getQuery() query} with the given {@code name}.
	 *
	 * @param name the name of the query to be compared.
	 * @return the predicate.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchQuery(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		return (request, cache) ->
				Objects.equals(
						request.getQuery().get(name),
						cache.getRequest().getQuery().get(name)
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests are equal.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchRequest() {
		return (request, cache) ->
				Objects.equals(
						request,
						cache.getRequest()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getRequestLine() request line}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchRequestLine() {
		return (request, cache) ->
				Objects.equals(
						request.getRequestLine(),
						cache.getRequest().getRequestLine()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getScheme() scheme}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchScheme() {
		return (request, cache) ->
				Objects.equals(
						request.getScheme(),
						cache.getRequest().getScheme()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getUri() uri}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchUri() {
		return (request, cache) ->
				Objects.equals(
						request.getUri(),
						cache.getRequest().getUri()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getUserInfo() user info}.
	 *
	 * @return the predicate.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchUserInfo() {
		return (request, cache) ->
				Objects.equals(
						request.getUserInfo(),
						cache.getRequest().getUserInfo()
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a predicate that returns {@code true} when the requests have an equal {@link
	 * Request#getUserInfo() user info} at the given {@code index}.
	 *
	 * @param index the index of the user info to be compared.
	 * @return the predicate.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator matchUserInfo(int index) {
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		return (request, cache) ->
				Objects.equals(
						request.getUserInfo().get(index),
						cache.getRequest().getUserInfo().get(index)
				) ?
				CacheValidity.VALID :
				CacheValidity.INVALID;
	}

	/**
	 * Return a validator that returns {@link CacheValidity#EXPIRED} if the cache's
	 * creation instant plus the given {@code timeout} is before {@link Instant#now()
	 * now}.
	 *
	 * @param timeout the cache timout.
	 * @return a new validator.
	 * @throws NullPointerException if the given {@code timeout} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator timeout(@NotNull TemporalAmount timeout) {
		Objects.requireNonNull(timeout, "timeout");
		return (request, cache) ->
				cache.getCreationInstant().plus(timeout).isAfter(Instant.now()) ?
				CacheValidity.VALID :
				CacheValidity.EXPIRED;
	}

	/**
	 * Return the default validator.
	 *
	 * @return the default validator.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator validator() {
		return CacheValidator.validator(
				CacheValidator.matchMethod(),
				CacheValidator.matchHost(),
				CacheValidator.matchPath(),
				CacheValidator.matchQuery(),
				CacheValidator.matchFragment(),
				CacheValidator.matchHeaders(),
				CacheValidator.matchBody(),
				CacheValidator.timeout(Duration.ofMinutes(10))
		);
	}

	/**
	 * A cache validator from combining the given {@code validators}.
	 * <br>
	 * The returned validator will return {@link CacheValidity#INVALID} or {@link
	 * CacheValidity#EXPIRED} when the first of the given {@code validators} returns one
	 * of them and will return {@link CacheValidity#VALID} when all the given {@code
	 * validators} returns {@link CacheValidity#VALID}.
	 *
	 * @param validators the validators to be combined.
	 * @return a validator combining the given {@code validators}.
	 * @throws NullPointerException if the given {@code validators} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	static CacheValidator validator(@Nullable CacheValidator @NotNull ... validators) {
		Objects.requireNonNull(validators, "validators");
		return (request, cache) -> {

			for (CacheValidator validator : validators)
				if (validator != null)
					switch (validator.match(request, cache)) {
						case INVALID:
							//invalid cache
							return CacheValidity.INVALID;
						case EXPIRED:
							//expired cache
							return CacheValidity.EXPIRED;
					}

			return CacheValidity.VALID;
		};
	}

	/**
	 * Return how valid the given {@code cache} is for the given {@code request}.
	 *
	 * @param request the request.
	 * @param cache   the cache to be validated.
	 * @return how valid the given {@code cache} is for the given {@code request}.
	 * @throws NullPointerException if the given {@code request} or {@code cache} is
	 *                              null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	CacheValidity match(@NotNull Request request, @NotNull Cache cache);
}
