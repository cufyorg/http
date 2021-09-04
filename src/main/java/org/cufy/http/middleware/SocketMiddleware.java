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
package org.cufy.http.middleware;

import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * A middleware that injects the {@link ConnectionCallback}. Made to listen for the {@link
 * Client#CONNECT} action and perform the connection using the {@link Socket} method.
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
	 * A global instance for {@link ConnectionCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Callback<Request> CALLBACK_CONNECTION = new ConnectionCallback();
	/**
	 * A global instance for {@link HeadersCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Callback<Request> CALLBACK_HEADERS = new HeadersCallback();

	/**
	 * A global instance of the middleware.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Middleware MIDDLEWARE = new SocketMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a socket middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware socketMiddleware() {
		return SocketMiddleware.MIDDLEWARE;
	}

	@Override
	public void inject(@NotNull Client client) {
		client.on(Client.CONNECT, SocketMiddleware.CALLBACK_CONNECTION);
		client.on(Client.SENDING, SocketMiddleware.CALLBACK_HEADERS);
	}

	/**
	 * A callback that do the work of the "CMW" (Connection Middle-ware) and manages the
	 * connection. This callback supports all the optional connection actions.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class ConnectionCallback implements Callback<Request> {
		@SuppressWarnings("OverlyLongMethod")
		@Override
		public void call(@NotNull Client client, @Nullable Request request) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(request, "request");

			//SENDING
			client.perform(Client.SENDING, request);

			String host = request.getHost().toString();
			int port = Integer.parseInt(request.getPort().toString());

			//noinspection OverlyLongLambda
			new Thread(() -> {
				String inMessage;

				try (
						Socket socket = new Socket(host, port);
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
				) {
					try {
						//sending...
						String outMessage = request.toString();

						writer.write(outMessage);
						writer.write("\r\n");
						writer.flush();

						//SENT
						client.perform(Client.SENT, outMessage);
					} catch (IOException e) {
						//NOT-SENT
						client.perform(Client.NOT_SENT, e);
						//do not continue
						return;
					}

					try {
						//receiving...
						StringBuilder builder = new StringBuilder();

						char[] buffer = new char[1024];
						while (true) {
							int l = reader.read(buffer);

							if (l > 0)
								builder.append(buffer, 0, l);
							else if (l < 0)
								break;
						}

						inMessage = builder.toString();

						//RECEIVING
						client.perform(Client.RECEIVING, inMessage);
					} catch (IOException e) {
						//NOT-RECEIVED
						client.perform(Client.NOT_RECEIVED, e);
						//do not continue
						return;
					}
				} catch (IOException e) {
					//DISCONNECTED
					client.perform(Client.DISCONNECTED, e);
					//do not continue
					return;
				}

				try {
					Response response = Response.response(inMessage);

					//RECEIVED
					client.perform(Client.RECEIVED, response);
					//CONNECTED
					client.perform(Client.CONNECTED, response);
				} catch (IllegalArgumentException e) {
					//MALFORMED
					client.perform(Client.MALFORMED, new IOException(e.getMessage(), e));
				}
			}).start();
		}
	}

	/**
	 * A callback that fill-out missing mandatory headers.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class HeadersCallback implements Callback<Request> {
		@Override
		public void call(@NotNull Client client, @Nullable Request request) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(request, "request");
			request.getHeaders()
				   .computeIfAbsent(
						   Headers.HOST,
						   () -> String.valueOf(request.getHost())
				   )
				   .computeIfAbsent(
						   Headers.DATE,
						   //https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
						   () ->
								   DateTimeFormatter.ofPattern(
										   "EEE, dd MMM yyyy HH:mm:ss O",
										   Locale.ENGLISH
								   ).format(ZonedDateTime.now())
				   )
				   .computeIfAbsent(
						   Headers.CONTENT_TYPE,
						   () -> request.getBody().getContentType()
				   )
				   .computeIfAbsent(
						   Headers.CONTENT_LENGTH,
						   () -> String.valueOf(request.getBody().getContentLength())
				   );
		}
	}
}
