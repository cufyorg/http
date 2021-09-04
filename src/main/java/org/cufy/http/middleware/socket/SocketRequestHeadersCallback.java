package org.cufy.http.middleware.socket;

import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * A callback that fill-out missing mandatory headers.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.23
 */
public class SocketRequestHeadersCallback implements Callback<Request> {
	/**
	 * A global instance for {@link SocketRequestHeadersCallback}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	private static final Callback<Request> INSTANCE = new SocketRequestHeadersCallback();

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a socket request headers callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> socketRequestHeadersCallback() {
		return SocketRequestHeadersCallback.INSTANCE;
	}

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
