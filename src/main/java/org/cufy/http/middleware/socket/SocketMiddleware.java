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
package org.cufy.http.middleware.socket;

import org.cufy.http.connect.Client;
import org.cufy.http.middleware.Middleware;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;

import static org.cufy.http.middleware.socket.SocketConnectionCallback.socketConnectionCallback;
import static org.cufy.http.middleware.socket.SocketRequestHeadersCallback.socketRequestHeadersCallback;

/**
 * A middleware that injects the {@link SocketConnectionCallback}. Made to listen for the
 * {@link Client#CONNECT} action and perform the connection using the {@link Socket}
 * method.
 * <br>
 * Actions:
 * <ul>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class SocketMiddleware implements Middleware {
	/**
	 * A global instance of the middleware.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	private static final Middleware INSTANCE = new SocketMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a socket middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware socketMiddleware() {
		return SocketMiddleware.INSTANCE;
	}

	@Override
	public void inject(@NotNull Client client) {
		client.on(Client.CONNECT, socketConnectionCallback());
		client.on(Client.SENDING, socketRequestHeadersCallback());
	}
}
