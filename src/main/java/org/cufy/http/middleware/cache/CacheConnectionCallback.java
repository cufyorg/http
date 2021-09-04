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
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A callback that triggers {@link Client#CONNECT} when no cache is available for the
 * request given to it. Otherwise, triggers {@link Client#CONNECTED} with the cached
 * response.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public class CacheConnectionCallback implements Callback<Request> {
	/**
	 * A global instance for {@link CacheConnectionCallback}.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	private static final Callback<Request> INSTANCE = new CacheConnectionCallback();

	/**
	 * The cache repo to be used by the callback.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final CacheRepository repository;

	/**
	 * Construct a new cache connection callback.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	public CacheConnectionCallback() {
		this.repository = new MemoryCacheRepository(CacheValidator.validator());
	}

	/**
	 * Construct a new cache connection callback with the given {@code repository}.
	 *
	 * @param repository the cache repo to be used by the constructed callback.
	 * @throws NullPointerException if the given {@code repository} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public CacheConnectionCallback(@NotNull CacheRepository repository) {
		Objects.requireNonNull(repository, "repository");
		this.repository = repository;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a cache connection callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> cacheConnectionCallback() {
		return CacheConnectionCallback.INSTANCE;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @param repository the repository to be used by the callback.
	 * @return a cache connection callback.
	 * @throws NullPointerException if the given {@code repository} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> cacheConnectionCallback(@NotNull CacheRepository repository) {
		return new CacheConnectionCallback(repository);
	}

	@Override
	public void call(@NotNull Client client, @Nullable Request request) {
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(request, "request");

		//restore cache
		Cache cache = this.repository.find(request);

		if (cache == null)
			//if no cache, connect to create new cache
			client.perform(Client.CONNECT, request);
		else
			//if there is a cache, do perform 'connected' with the cached response
			client.perform(Client.CONNECTED, cache.getResponse());
	}
}
