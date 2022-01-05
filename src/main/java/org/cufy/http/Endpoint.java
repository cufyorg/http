/*
 *	Copyright 2021-2022 Cufy
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
package org.cufy.http;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An endpoint is an object representing an endpoint's configurations between the server
 * and the client.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface Endpoint {
	/**
	 * The default endpoint when there is no endpoint to be used.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	Endpoint UNSPECIFIED = new Endpoint() {
	};

	/**
	 * Prepare the given {@code request} to be consumed.
	 *
	 * @param request the request to be prepared.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@Contract(mutates = "param")
	default void accept(@NotNull Request request) {
	}

	/**
	 * Prepare the given {@code response} to be consumed.
	 *
	 * @param response the response to be prepared.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@Contract(mutates = "param")
	default void accept(@NotNull Response response) {
	}

	/**
	 * Prepare the given {@code request} to be built.
	 *
	 * @param request the request to prepare.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@Contract(mutates = "param")
	default void prepare(@NotNull Request request) {
	}

	/**
	 * Prepare the given {@code response} to be built.
	 *
	 * @param response the response to prepare.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@Contract(mutates = "param")
	default void prepare(@NotNull Response response) {
	}
}
