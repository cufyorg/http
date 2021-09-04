package org.cufy.http.middleware.socket;

import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A callback that do the work of the "CMW" (Connection Middle-ware) and manages the
 * connection. This callback supports all the optional connection actions.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.23
 */
public class SocketConnectionCallback implements Callback<Request> {
	/**
	 * A global instance for {@link SocketConnectionCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	private static final Callback<Request> INSTANCE = new SocketConnectionCallback();

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a socket connection callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> socketConnectionCallback() {
		return SocketConnectionCallback.INSTANCE;
	}

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
