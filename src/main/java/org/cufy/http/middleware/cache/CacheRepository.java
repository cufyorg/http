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

/**
 * Cache repository manager.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public interface CacheRepository {

	/**
	 * Cache the given {@code request} and its {@code response}.
	 *
	 * @param request  the request to be cached.
	 * @param response the response to be cached.
	 * @return true, if the caching succeed.
	 * @throws NullPointerException if the given {@code request} or {@code response} is
	 *                              null.
	 * @since 0.2.11 ~2021.09.04
	 */
	boolean cache(@NotNull Request request, Response response);

	/**
	 * Find a cached response for the given {@code request}.
	 *
	 * @param request the request to find a cached response for.
	 * @return the cached response for the given {@code request} or null if none or
	 * 		failed.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@Nullable
	Cache find(@NotNull Request request);
}
