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
package org.cufy.json;

import org.cufy.json.token.JsonContextToken;
import org.cufy.json.token.JsonTokenException;
import org.cufy.json.token.JsonTokenSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import static java.lang.Math.min;

/**
 * Json utility class.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
public final class Json {
	/**
	 * The json true constant.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	public static final JsonBoolean FALSE = new JsonBoolean(false);
	/**
	 * The json null constant.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	public static final JsonNull NULL = new JsonNull();
	/**
	 * The json false constant.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	public static final JsonBoolean TRUE = new JsonBoolean(true);

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.11.23
	 */
	private Json() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Construct a new json element from parsing the given {@code string}.
	 *
	 * @param string the source string to be parsed.
	 * @return a new json element from parsing the given value.
	 * @throws NullPointerException     if the given {@code string} is null.
	 * @throws IllegalArgumentException if the given {@code string} is invalid json.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonElement parse(@NotNull @Language("json") String string) {
		Objects.requireNonNull(string, "string");

		try {
			return new JsonContextToken(new JsonTokenSource(new StringReader(string)))
					.nextElement();
		} catch (JsonTokenException exception) {
			//noinspection NumericCastThatLosesPrecision
			int i = (int) exception.index();
			int l = string.length();
			throw new IllegalArgumentException(
					exception.getMessage() + ": " +
					string.substring(min(0, i - 10), min(l, i)) +
					"<" + (i < l ? string.charAt(i) : "") + ">" +
					string.substring(min(l, i + 1), min(l, i + 11)),
					exception
			);
		} catch (IOException e) {
			throw new InternalError(e);
		}
	}
}
