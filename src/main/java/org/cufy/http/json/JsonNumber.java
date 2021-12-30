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
package org.cufy.http.json;

import org.cufy.http.json.token.JsonTokenException;
import org.cufy.http.json.token.JsonTokenSource;
import org.cufy.http.json.token.JsonNumberToken;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Json number constant class.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonNumber implements JsonElement {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8423875023841804519L;

	/**
	 * The value of this number.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	protected final BigDecimal number;

	/**
	 * Construct a new json number.
	 *
	 * @param number the number to be the number of the constructed number.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonNumber(@NotNull BigDecimal number) {
		Objects.requireNonNull(number, "number");
		this.number = number;
	}

	/**
	 * Construct a new json number from the given {@code number}.
	 *
	 * @param number the value for the constructed json number.
	 * @return a new json number from the given value.
	 * @throws NullPointerException if the given {@code number} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonNumber from(@NotNull BigDecimal number) {
		return new JsonNumber(number);
	}

	/**
	 * Construct a new json number from the given {@code number}.
	 *
	 * @param number the value for the constructed json number.
	 * @return a new json number from the given value.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonNumber from(long number) {
		return new JsonNumber(new BigDecimal(number));
	}

	/**
	 * Construct a new json number from the given {@code number}.
	 *
	 * @param number the value for the constructed json number.
	 * @return a new json number from the given value.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonNumber from(double number) {
		return new JsonNumber(new BigDecimal(number));
	}

	/**
	 * Construct a new json number from parsing the given {@code source}.
	 *
	 * @param source the source string to be parsed.
	 * @return a new json number from parsing the given source.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is invalid json number.
	 * @since 0.3.0 ~2021.12.15
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonNumber parse(@NotNull @Language("json") String source) {
		Objects.requireNonNull(source, "source");
		try {
			return new JsonNumberToken(new JsonTokenSource(new StringReader(source)))
					.nextElement();
		} catch (JsonTokenException e) {
			throw new IllegalArgumentException(e.formatMessage(source), e);
		} catch (IOException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public JsonNumber clone() {
		try {
			return (JsonNumber) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonNumber) {
			JsonNumber number = (JsonNumber) object;

			return this.number.compareTo(number.number) == 0;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.number.hashCode();
	}

	@NotNull
	@Override
	public String json() {
		return this.number.toString();
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		return this.number.toString();
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}

	/**
	 * Return the number backing this.
	 *
	 * @return the number backing this.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract(pure = true)
	public BigDecimal value() {
		return this.number;
	}
}
