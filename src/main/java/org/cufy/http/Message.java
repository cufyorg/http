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
package org.cufy.http;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * An interface for models with a body and headers.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.25
 */
public abstract class Message implements Cloneable, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3993457329839635619L;

	/**
	 * The body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	@Nullable
	protected Body body;
	/**
	 * The headers.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	@SuppressWarnings("NotNullFieldNotInitialized")
	@NotNull
	protected Headers headers;

	/**
	 * Capture this message into a new object.
	 *
	 * @return a clone of this message.
	 * @since 0.3.0 ~2021.11.26
	 */
	@Override
	public Message clone() {
		try {
			Message clone = (Message) super.clone();
			clone.headers = this.headers.clone();
			clone.body = this.body == null ? null : this.body.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Get the body of this message.
	 *
	 * @return the body of this.
	 * @since 0.3.0 ~2021.11.26
	 */
	@Nullable
	@Contract(pure = true)
	public Body getBody() {
		return this.body;
	}

	/**
	 * Get the headers of this message.
	 *
	 * @return the headers of this.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(pure = true)
	public Headers getHeaders() {
		return this.headers;
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this message does not support changing its
	 *                                       body.
	 * @since 0.3.0 ~2021.11.26
	 */
	@Contract(mutates = "this")
	public void setBody(@Nullable Body body) {
		this.body = body;
	}

	/**
	 * Set the headers of this from the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if this message does not support changing its
	 *                                       headers.
	 * @since 0.3.0 ~2021.11.26
	 */
	@Contract(mutates = "this")
	public void setHeaders(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
	}
}
