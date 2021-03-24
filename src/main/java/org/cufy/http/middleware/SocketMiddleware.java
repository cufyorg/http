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

import org.cufy.http.Client;
import org.cufy.http.component.Headers;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.util.Callback;
import org.cufy.http.util.Caller;
import org.cufy.http.util.Middleware;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.io.*;
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
public class SocketMiddleware implements Middleware<Client> {
	/**
	 * A global instance for {@link ConnectionCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Callback<Client, Request> CALLBACK_CONNECTION = new ConnectionCallback();
	/**
	 * A global instance for {@link ReformatCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Callback<Client, Request> CALLBACK_REFORMAT = new ReformatCallback();
	/**
	 * A global instance for the {@link StatusCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Callback<Client, Response> CALLBACK_STATUS = new StatusCallback();

	/**
	 * A global instance of the middleware.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Middleware<Client> MIDDLEWARE = new SocketMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a socket middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middleware() {
		return SocketMiddleware.MIDDLEWARE;
	}

	@Override
	public void inject(Caller<Client> caller) {
		if (caller instanceof Client) {
			caller.on(Client.REFORMAT, SocketMiddleware.CALLBACK_REFORMAT);
			caller.ont(Client.CONNECT, SocketMiddleware.CALLBACK_CONNECTION);
			caller.on(Client.CONNECTED, SocketMiddleware.CALLBACK_STATUS);

			return;
		}

		throw new IllegalArgumentException("SocketMiddleware is made for Clients");
	}

	/**
	 * A callback for the action {@link Client#CONNECT} that does the connection when
	 * called and invoke {@link Client#CONNECTED} when the response got parsed
	 * successfully. Also, invokes the {@link Client#NOT_SENT} or {@link
	 * Client#NOT_RECEIVED} or {@link Client#MALFORMED} when a problem occurs depending on
	 * the problem's type.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class ConnectionCallback implements Callback<Client, Request> {
		@Override
		public void call(@NotNull Client client, Request request) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(request, "request");

			client.trigger(request, Client.REFORMAT);

			String host = request.host().toString();
			int port = Integer.parseInt(request.port().toString());

			StringBuilder builder = new StringBuilder();

			try (
					Socket socket = new Socket(host, port);
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
			) {
				//sending...
				writer.write(request.toString());
				writer.write("\r\n");
				writer.flush();

				//receiving...
				try {
					char[] buffer = new char[1024];
					while (true) {
						int l = reader.read(buffer);

						if (l > 0)
							builder.append(buffer, 0, l);
						else if (l < 0)
							break;
					}
				} catch (IOException e) {
					client.trigger(e, Client.NOT_RECEIVED);
					//do not continue
					return;
				}
			} catch (IOException e) {
				client.trigger(e, Client.NOT_SENT);
				//do not continue
				return;
			}

			try {
				@Subst("HTTP/1.1 200 OK\n") String source = builder.toString();
				Response response = Response.parse(source);

				client.trigger(response, Client.CONNECTED);
			} catch (IllegalArgumentException e) {
				client.trigger(e, Client.MALFORMED);
			}
		}
	}

	/**
	 * A callback for the action {@link Client#REFORMAT}. Makes sure that the request has
	 * the headers {@link Headers#HOST}, {@link Headers#CONTENT_LENGTH} and {@link
	 * Headers#DATE} are set. (might add/remove some headers)
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class ReformatCallback implements Callback<Client, Request> {
		@Override
		public void call(@NotNull Client caller, Request request) {
			Objects.requireNonNull(caller, "caller");
			Objects.requireNonNull(request, "request");
			request.headers()
					.computeIfAbsent(
							Headers.HOST,
							() -> " " + request.requestLine().uri().authority().host()
					)
					.computeIfAbsent(
							Headers.CONTENT_LENGTH,
							() -> " " + request.body().length()
					)
					.computeIfAbsent(
							Headers.DATE,
							//https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
							() -> " " +
								  DateTimeFormatter.ofPattern(
										  "EEE, dd MMM yyyy HH:mm:ss O",
										  Locale.ENGLISH
								  ).format(ZonedDateTime.now())
					);
		}
	}

	/**
	 * A callback for the action {@link Client#CONNECTED} that depending on the
	 * status-code name of the response, invokes the suitable action of {@link
	 * Client#S1XX}, {@link Client#S2XX}, {@link Client#S3XX}, {@link Client#S4XX} or
	 * {@link Client#S5XX}.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class StatusCallback implements Callback<Client, Response> {
		@Override
		public void call(@NotNull Client caller, Response response) {
			Objects.requireNonNull(caller, "caller");
			Objects.requireNonNull(response, "response");
			int statusCode = Integer.parseInt(response.statusLine().statusCode().toString());

			String action;
			switch (statusCode / 100) {
				case 1:
					action = Client.S1XX;
					break;
				case 2:
					action = Client.S2XX;
					break;
				case 3:
					action = Client.S3XX;
					break;
				case 4:
					action = Client.S4XX;
					break;
				case 5:
					action = Client.S5XX;
					break;
				default:
					//not my problem!
					return;
			}

			caller.trigger(response, action);
		}
	}
}
