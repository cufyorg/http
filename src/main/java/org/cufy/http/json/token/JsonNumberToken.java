/*
 *	Copyright 2021-2022 Cufy
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

import org.cufy.http.json.JsonNumber;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Json number parsing token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public class JsonNumberToken extends AbstractJsonToken {
	/**
	 * Construct a new json number token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonNumberToken(@NotNull JsonTokenSource source) {
		super(source);
	}

	@NotNull
	@Override
	public JsonNumber nextElement() throws IOException {
		BigDecimal number = this.nextNumber();

		return new JsonNumber(number);
	}

	/**
	 * Return the next number.
	 *
	 * @return the next number.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the number is invalid.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(mutates = "this")
	public BigDecimal nextNumber() throws IOException {
		StringBuilder builder = new StringBuilder();

		while (true) {
			int read = this.maybeNextNumberChar();

			if (read == -1)
				try {
					return new BigDecimal(builder.toString());
				} catch (NumberFormatException e) {
					throw new JsonTokenException(e, this.source.nextIndex());
				}

			builder.append((char) read);
		}
	}

	/**
	 * Return the next character if it is a number character. Backoff when not a number
	 * character and return {@code -1}.
	 *
	 * @return the next character. Or {@code -1} if the next character is not a number
	 * 		character.
	 * @throws IOException when any i/o exception occurs.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(mutates = "this")
	public int maybeNextNumberChar() throws IOException {
		this.source.mark(1);

		int read = this.source.read();

		if (read < 0)
			return -1;

		if (
				read >= '0' && read <= '9' ||
				read == 'e' || read == 'E' ||
				read == '-' || read == '+' ||
				read == '.'
		)
			return read;

		this.source.reset();

		return -1;
	}
}
