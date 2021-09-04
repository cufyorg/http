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
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A cache repository that stores the caches on the memory.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public class MemoryCacheRepository implements CacheRepository {
	/**
	 * The caches list of the repository.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final List<Cache> caches = new ArrayList<>();
	/**
	 * The cache validator.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final CacheValidator validator;

	/**
	 * Construct a new cache repository that stores the caches in the memory.
	 *
	 * @param validator the validator.
	 * @throws NullPointerException if the given {@code validator} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public MemoryCacheRepository(@NotNull CacheValidator validator) {
		Objects.requireNonNull(validator, "validator");
		this.validator = validator;
	}

	@Override
	public boolean cache(@NotNull Request request, Response response) {
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");

		//loop to clean
		Iterator<Cache> iterator = this.caches.iterator();
		while (iterator.hasNext()) {
			Cache cache = iterator.next();

			switch (this.validator.match(request, cache)) {
				case EXPIRED:
					//remove expired cache
					iterator.remove();
			}
		}

		//add the cache
		this.caches.add(Cache.cache(request, response, Instant.now()));
		return true;
	}

	@Nullable
	@Override
	public Cache find(@NotNull Request request) {
		Objects.requireNonNull(request, "request");

		//loop to find valid cache + clean
		Iterator<Cache> iterator = this.caches.iterator();
		while (iterator.hasNext()) {
			Cache cache = iterator.next();

			switch (this.validator.match(request, cache)) {
				case VALID:
					//found valid cache
					return cache;
				case EXPIRED:
					//remove expired cache
					iterator.remove();
			}
		}

		return null;
	}
}
