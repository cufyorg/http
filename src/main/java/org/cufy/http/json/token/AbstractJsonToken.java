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
package org.cufy.http.json.token;

import org.cufy.http.json.JsonElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * A class containing common functions a json token might need.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public abstract class AbstractJsonToken implements JsonToken {
	/**
	 * The source reader.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	protected final JsonTokenSource source;

	/**
	 * Construct a new token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	protected AbstractJsonToken(@NotNull JsonTokenSource source) {
		Objects.requireNonNull(source, "source");
		this.source = source;
	}

	/**
	 * Peek the next character.
	 *
	 * @return the next character.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if reached EOF.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(pure = true)
	protected int maybePeekChar() throws IOException {
		this.source.mark(1);
		int read = this.source.read();

		if (read == -1)
			return -1;

		this.source.reset();
		return read;
	}

	/**
	 * Return the next character.
	 *
	 * @return the next character.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if reached EOF.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(mutates = "this")
	protected char nextChar() throws IOException {
		int read = this.source.read();

		if (read == -1)
			throw new JsonTokenException(
					"Unexpected EOF",
					this.source.nextIndex()
			);

		return (char) read;
	}

	/**
	 * Return the next child element.
	 *
	 * @return the next child element.
	 * @throws IOException        when any I/O exception occurs.
	 * @throws JsonTokenException when the token is invalid.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(mutates = "this")
	protected JsonElement nextChildElement() throws IOException {
		char c = this.peekChar();

		if (c == '\"')
			return new JsonStringToken(this.source).nextElement();
		if (c >= '0' && c <= '9' || c == '-' || c == '+')
			return new JsonNumberToken(this.source).nextElement();
		if (c == '{')
			return new JsonObjectToken(this.source).nextElement();
		if (c == '[')
			return new JsonArrayToken(this.source).nextElement();
		if (c == 't' || c == 'f')
			return new JsonBooleanToken(this.source).nextElement();
		if (c == 'n')
			return new JsonNullToken(this.source).nextElement();

		this.nextChar();

		throw new JsonTokenException(
				"Unexpected token",
				this.source.nextIndex()
		);
	}

	/**
	 * Read until the next char is not a whitespace.
	 *
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if reached EOF.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(mutates = "this")
	protected void nextWhitespace() throws IOException {
		while (true)
			switch (this.maybePeekChar()) {
				case ' ':
				case '\n':
				case '\r':
				case '\t':
					this.nextChar();
					break;
				default:
					return;
			}
	}

	/**
	 * Peek the next character.
	 *
	 * @return the next character.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if reached EOF.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(pure = true)
	protected char peekChar() throws IOException {
		this.source.mark(1);
		int read = this.source.read();

		if (read == -1)
			throw new JsonTokenException(
					"Unexpected EOF",
					this.source.nextIndex()
			);

		this.source.reset();
		return (char) read;
	}
}
