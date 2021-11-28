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

import org.cufy.json.JsonElement;
import org.cufy.json.JsonObject;
import org.cufy.json.JsonString;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json object parsing token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public class JsonObjectToken extends AbstractJsonToken {
	/**
	 * Construct a new object token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonObjectToken(@NotNull JsonTokenSource source) {
		super(source);
	}

	@NotNull
	@Override
	public JsonObject nextElement() throws IOException {
		Map<@NotNull JsonString, @NotNull JsonElement> map = this.nextMap();

		return new JsonObject(map);
	}

	/**
	 * Return the next map.
	 *
	 * @return the next map.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the map is invalid.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(mutates = "this")
	public Map<@NotNull JsonString, @NotNull JsonElement> nextMap() throws IOException {
		if (this.nextChar() != '{')
			throw new JsonTokenException(
					"Expected: {",
					this.source.nextIndex()
			);

		Map<@NotNull JsonString, @NotNull JsonElement> map = new LinkedHashMap<>();

		/* true, when the value is separated */
		boolean separated = true;

		while (true) {
			char c = this.peekChar();

			switch (c) {
				case '}':
					this.nextChar();
					return map;
				case ',':
					this.nextChar();
					throw new JsonTokenException(
							"Misplaced comma",
							this.source.nextIndex()
					);
				default:
					if (!separated)
						throw new JsonTokenException(
								"Expected: ,",
								this.source.nextIndex()
						);

					this.nextWhitespace();

					JsonElement key = this.nextChildElement();

					if (!(key instanceof JsonString))
						throw new JsonTokenException(
								"Keys in objects must be strings",
								this.source.nextIndex()
						);

					this.nextWhitespace();

					if (this.nextChar() != ':')
						throw new JsonTokenException(
								"Unexpected token", this.source.nextIndex()
						);

					this.nextWhitespace();

					JsonElement value = this.nextChildElement();

					map.put((JsonString) key, value);

					this.nextWhitespace();

					if (this.peekChar() == ',')
						this.nextChar();
					else
						separated = false;

					break;
			}
		}
	}
}
