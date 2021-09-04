package org.cufy.http.middleware.okhttp;

import okhttp3.*;
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.request.HttpVersion;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * The callback responsible for the http connection when the action {@link Client#CONNECT}
 * get triggered.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.24
 */
public class OkHttpConnectionCallback implements Callback<Request> {
	/**
	 * A global instance for {@link OkHttpConnectionCallback}.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	private static final Callback<Request> INSTANCE = new OkHttpConnectionCallback();

	/**
	 * The client used by this callback.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	protected final OkHttpClient client;

	/**
	 * Construct a new connection callback that uses a new ok http client.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	public OkHttpConnectionCallback() {
		this.client = new OkHttpClient();
	}

	/**
	 * Construct a new connection callback that uses the given ok http {@code client}.
	 *
	 * @param client the ok http client to be used by the constructed callback.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public OkHttpConnectionCallback(@NotNull OkHttpClient client) {
		Objects.requireNonNull(client, "client");
		this.client = client;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return an okhttp connection callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> okHttpConnectionCallback() {
		return OkHttpConnectionCallback.INSTANCE;
	}

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @param client the client to be used by the callback.
	 * @return an okhttp connection callback.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Request> okHttpConnectionCallback(@NotNull OkHttpClient client) {
		return new OkHttpConnectionCallback(client);
	}

	@Override
	public void call(@NotNull Client client, @Nullable Request request) {
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(request, "request");

		//SENDING
		client.perform(Client.SENDING, request);

		okhttp3.Request okRequest = new okhttp3.Request.Builder()
				.method(
						request.getMethod().toString(),
						Optional.ofNullable(request.getHeaders().get(Headers.CONTENT_TYPE))
								.map(MediaType::parse)
								.map(contentType -> RequestBody.create(request.getBody().toString(), contentType))
								.orElse(null)
				)
				.url(request.getUri().toString())
				.headers(okhttp3.Headers.of(request.getHeaders().values()))
				.build();

		//noinspection ParameterNameDiffersFromOverriddenParameter
		this.client.newCall(okRequest).enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException exception) {
				//DISCONNECTED
				client.perform(Client.DISCONNECTED, exception);
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull okhttp3.Response okResponse) throws IOException {
				try (
						okhttp3.Response okr = okResponse;
						ResponseBody body = okResponse.body()
				) {
					//noinspection ConstantConditions
					Response response = Response
							.response()
							.setHttpVersion(HttpVersion.raw(okResponse.protocol().toString()))
							.setStatusCode(Integer.toString(okResponse.code()))
							.setReasonPhrase(okResponse.message())
							.setHeaders(okResponse.headers().toString())
							.setBody(body.string());

					//RECEIVED
					client.perform(Client.RECEIVED, response);
					//CONNECTED
					client.perform(Client.CONNECTED, response);
				} catch (IllegalArgumentException e) {
					//MALFORMED
					client.perform(Client.MALFORMED, new IOException(e.getMessage(), e));
				}
			}
		});
	}
}
