/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.client;

import org.cufy.http.client.wrapper.ClientRequest;
import org.cufy.http.client.wrapper.ClientResponse;

/**
 * Utility class containing constant {@link Action Actions}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.13
 */
public final class On {
	/**
	 * An action that gets triggered when the connection must be started.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public static final Action<ClientRequest<?>> CONNECT = (name, parameter) ->
			"connect".equals(name) && parameter instanceof ClientRequest;
	/**
	 * An action that gets triggered by the connection when the connection is done.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public static final Action<ClientResponse<?>> CONNECTED = (name, parameter) ->
			"connected".equals(name) && parameter instanceof ClientResponse;
	/**
	 * An action that gets triggered by the connection when the connection fails.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public static final Action<ClientRequest<?>> DISCONNECTED = (name, parameter) ->
			"disconnected".equals(name) && parameter instanceof ClientRequest;
	/**
	 * An action that gets triggered by the connection right before performing the
	 * connection.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final Action<ClientRequest<?>> REQUEST = (name, parameter) ->
			"request".equals(name) && parameter instanceof ClientRequest;
	/**
	 * An action that gets triggered by the connection right after performing the
	 * connection.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final Action<ClientResponse<?>> RESPONSE = (name, parameter) ->
			"response".equals(name) && parameter instanceof ClientResponse;

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.12.13
	 */
	private On() {
		throw new AssertionError("No instance for you!");
	}
}
