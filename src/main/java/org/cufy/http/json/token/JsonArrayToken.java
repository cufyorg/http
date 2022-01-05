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

import org.cufy.http.json.JsonElement;
import org.cufy.http.json.JsonArray;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Json array parsing token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public class JsonArrayToken extends AbstractJsonToken {
	/**
	 * Construct a new json array token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonArrayToken(@NotNull JsonTokenSource source) {
		super(source);
	}

	@NotNull
	@Override
	public JsonArray nextElement() throws IOException {
		List<@NotNull JsonElement> list = this.nextList();

		return new JsonArray(list);
	}

	/**
	 * Return the next list.
	 *
	 * @return the next list.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the list is invalid.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(mutates = "this")
	public List<@NotNull JsonElement> nextList() throws IOException {
		if (this.nextChar() != '[')
			throw new JsonTokenException(
					"Expected: [",
					this.source.nextIndex()
			);

		List<@NotNull JsonElement> list = new LinkedList<>();

		/* true, when the value is separated */
		boolean separated = true;

		while (true) {
			char c = this.peekChar();

			switch (c) {
				case ']':
					this.nextChar();
					return list;
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

					JsonElement element = this.nextChildElement();

					list.add(element);

					if (this.peekChar() == ',')
						this.nextChar();
					else
						separated = false;

					break;
			}
		}
	}
}
