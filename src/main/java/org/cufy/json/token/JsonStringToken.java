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
package org.cufy.json.token;

import org.cufy.json.JsonString;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Json string parsing token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Internal
public class JsonStringToken extends AbstractJsonToken {
	/**
	 * Construct a new json string token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonStringToken(@NotNull JsonTokenSource source) {
		super(source);
	}

	@NotNull
	@Override
	public JsonString nextElement() throws IOException {
		String string = this.nextString();

		return new JsonString(string);
	}

	/**
	 * Return the next string.
	 *
	 * @return the next string.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the string is invalid.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(mutates = "this")
	public String nextString() throws IOException {
		if (this.nextChar() != '"')
			throw new JsonTokenException("Expected: \"", this.source.nextIndex());

		StringBuilder builder = new StringBuilder();

		while (true) {
			char c = this.nextChar();

			switch (c) {
				case '\\':
					String escaped = this.nextEscaped();

					builder.append(escaped);
					continue;
				case '"':
					return builder.toString();
				default:
					builder.append(c);
			}
		}
	}

	/**
	 * Return the next encoded character.
	 *
	 * @return the next encoded character.
	 * @throws IOException        if any i/o exception occurs.
	 * @throws JsonTokenException if the encoding is invalid.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(mutates = "this")
	protected char nextEncoded() throws IOException {
		char[] buffer = {0, 0, 0, 0};
		int index = 0;

		while (true) {
			char c = this.nextChar();

			if (c >= '0' && c <= '9' ||
				c >= 'A' && c <= 'F' ||
				c >= 'a' && c <= 'f') {
				buffer[index++] = c;

				if (index >= buffer.length)
					return (char) Integer.parseInt(String.valueOf(buffer), 16);

				continue;
			}

			throw new JsonTokenException("Encoded char must be in hex", this.source.nextIndex());
		}
	}

	/**
	 * Return the next escaped sequence.
	 *
	 * @return the next escaped sequence.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the escaping is invalid.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(mutates = "this")
	protected String nextEscaped() throws IOException {
		char c = this.nextChar();

		switch (c) {
			case '"':
				return "\"";
			case '\\':
				return "\\";
			case 'b':
				return "\b";
			case 'f':
				return "\f";
			case 'n':
				return "\n";
			case 'r':
				return "\r";
			case 't':
				return "\t";
			case 'u':
				return Character.toString(this.nextEncoded());
			default:
				throw new JsonTokenException("Invalid escaped char", this.source.nextIndex());
		}
	}
}
