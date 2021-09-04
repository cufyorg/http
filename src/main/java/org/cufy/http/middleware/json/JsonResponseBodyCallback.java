package org.cufy.http.middleware.json;

import org.cufy.http.body.json.JsonBody;
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * A callback that parses the body into a json-body.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.24
 */
public class JsonResponseBodyCallback implements Callback<Response> {
	/**
	 * A global instance for {@link JsonResponseBodyCallback}.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	private static final Callback<Response> INSTANCE = new JsonResponseBodyCallback();

	/**
	 * Return a usable callback for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json parse-response callback.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Callback<Response> jsonResponseBodyCallback() {
		return JsonResponseBodyCallback.INSTANCE;
	}

	@Override
	public void call(@NotNull Client client, @Nullable Response response) {
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(response, "response");
		try {
			String contentType = response.getHeaders().get(Headers.CONTENT_TYPE);

			//noinspection DynamicRegexReplaceableByCompiledPattern
			if (contentType != null &&
				contentType.matches("^(?:application|text)\\/(x-)?json.*$"))
				response.body(JsonBody::json);
		} catch (IllegalArgumentException e) {
			client.perform(Client.NOT_PARSED, new IOException(e.getMessage(), e));
		}
	}
}
