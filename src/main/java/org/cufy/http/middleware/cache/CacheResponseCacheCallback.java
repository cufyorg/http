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

import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A callback that caches the response given to it.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public class CacheResponseCacheCallback implements Callback<Response> {
	/**
	 * A global instance for {@link CacheResponseCacheCallback}.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	private static final Callback<Response> INSTANCE = new CacheResponseCacheCallback();

	/**
	 * The cache repo to be used by the callback.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final CacheRepository repository;

	/**
	 * Construct a new cache response-cache callback.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	public CacheResponseCacheCallback() {
		this.repository = new MemoryCacheRepository(CacheValidator.validator());
	}

	/**
	 * Construct a new cache response-cache callback.
	 *
	 * @param repository the cache repo to be used by the constructed callback.
	 * @throws NullPointerException if the given {@code repository} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public CacheResponseCacheCallback(@NotNull CacheRepository repository) {
		Objects.requireNonNull(repository, "repository");
		this.repository = repository;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a cache response-cache callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Response> cacheResponseCacheCallback() {
		return CacheResponseCacheCallback.INSTANCE;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @param repository the repository to be used by the callback.
	 * @return a cache response-cache callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Response> cacheResponseCacheCallback(@NotNull CacheRepository repository) {
		return new CacheResponseCacheCallback(repository);
	}

	@Override
	public void call(@NotNull Client client, @Nullable Response response) {
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(response, "response");

		//get request
		Request request = client.getRequest();

		//cache request and response
		this.repository.cache(request, response);
	}
}
