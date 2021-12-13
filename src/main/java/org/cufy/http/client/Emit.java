package org.cufy.http.client;

import org.cufy.http.client.wrapper.ClientRequest;
import org.cufy.http.client.wrapper.ClientResponse;

/**
 * Utility class containing constant {@link Emission Emissions}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.13
 */
public final class Emit {
	/**
	 * An emission that triggers the connection to start.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<ClientRequest<?>> CONNECT = new Emission<>("connect");
	/**
	 * An emission that triggers the connection consumer to consume the result.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<ClientResponse<?>> CONNECTED = new Emission<>("connected");
	/**
	 * An emission that triggers the connection consumer to handle a connection failure.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<ClientRequest<?>> DISCONNECTED = new Emission<>("disconnected");
	/**
	 * An emission that triggers the connection consumer right before performing the
	 * connection.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<ClientRequest<?>> REQUEST = new Emission<>("request");
	/**
	 * An emission that triggers the connection consumer right after performing the
	 * connection.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<ClientResponse<?>> RESPONSE = new Emission<>("response");

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.12.13
	 */
	private Emit() {
		throw new AssertionError("No instance for you!");
	}
}
