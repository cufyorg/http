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
package org.cufy.http.model;

import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * <b>Implementation Specific</b>
 * <br>
 * The "Body" part of the request. Uses {@link Query} as the parameters to maximize
 * compatibility between identical components.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public interface Body extends Cloneable, Serializable {
	/**
	 * The length of this body (the length of the bytes).
	 *
	 * @return the length of this body.
	 * @throws IOError when any I/O error occurs while attempting to read the length of
	 *                 the content.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(pure = true)
	@Range(from = 0, to = Long.MAX_VALUE)
	default long getContentLength() {
		byte[] buffer = new byte[1024];
		long length = 0;

		try (InputStream is = this.openInputStream()) {
			while (true) {
				long read = is.read(buffer, 0, buffer.length);

				if (read < 0)
					return length;

				length += read;
			}
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * The content type of this body. (null=no content)
	 *
	 * @return the content type of this body.
	 * @throws IOError when any I/O error occurs while attempting to read the type of the
	 *                 content.
	 * @since 0.0.1 ~2021.03.30
	 */
	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Contract(pure = true)
	default String getContentType() {
		return null;
	}

	/**
	 * Capture this body into a new object.
	 *
	 * @return a clone of this body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Body clone();

	/**
	 * Two bodies are always equal when they are the same object. Other types of equality
	 * checks are left for subclasses to specify.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a body and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a body is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this body.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Body. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Content
	 * </pre>
	 * Example:
	 * <pre>
	 *     {name: john}
	 * </pre>
	 *
	 * @return a string representation of this body.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Return a new input stream over the bytes of this body.
	 *
	 * @return an input stream of the bytes of this body.
	 * @throws IOError when any I/O error occurs while attempting to open an input stream
	 *                 over the content of this body.
	 * @since 0.3.0 ~2021.11.15
	 */
	@NotNull
	@Contract(pure = true)
	InputStream openInputStream();
}
