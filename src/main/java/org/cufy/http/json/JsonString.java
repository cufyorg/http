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
package org.cufy.http.json;

import org.cufy.http.json.token.JsonStringToken;
import org.cufy.http.json.token.JsonTokenException;
import org.cufy.http.json.token.JsonTokenSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

/**
 * Json string constant class.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonString implements JsonElement {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8423875023841804519L;

	/**
	 * The value of this string.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	protected final String string;

	/**
	 * Construct a new json string.
	 *
	 * @param string the string to be the string of the constructed string.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonString(@NotNull String string) {
		Objects.requireNonNull(string, "string");
		this.string = string;
	}

	/**
	 * Construct a new json string from parsing the given {@code source}.
	 *
	 * @param source the source string to be parsed.
	 * @return a new json string from parsing the given source.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is invalid json
	 *                                  string.
	 * @since 0.3.0 ~2021.12.15
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonString parse(@NotNull @Language("json") String source) {
		Objects.requireNonNull(source, "source");
		try {
			return new JsonStringToken(new JsonTokenSource(new StringReader(source)))
					.nextElement();
		} catch (JsonTokenException e) {
			throw new IllegalArgumentException(e.formatMessage(source), e);
		} catch (IOException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public JsonString clone() {
		try {
			return (JsonString) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonString) {
			JsonString string = (JsonString) object;

			return Objects.equals(this.string, string.string);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.string.hashCode();
	}

	@NotNull
	@Override
	public String json() {
		return "\"" +
			   this.string
					   .replace("\\", "\\\\")
					   // .replace("/", "\\/")
					   .replace("\"", "\\\"")
					   .replace("\b", "\\b")
					   .replace("\f", "\\f")
					   .replace("\n", "\\n")
					   .replace("\r", "\\r")
					   .replace("\t", "\\t") +
			   "\"";
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		return "\"" +
			   this.string
					   .replace("\\", "\\\\")
					   // .replace("/", "\\/")
					   .replace("\"", "\\\"")
					   .replace("\b", "\\b")
					   .replace("\f", "\\f")
					   .replace("\n", "\\n")
					   .replace("\r", "\\r")
					   .replace("\t", "\\t") +
			   "\"";
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}

	/**
	 * Return the string backing this json string.
	 *
	 * @return the string value of this.
	 * @since 0.3.0 ~2021.12.07
	 */
	@NotNull
	@Contract(pure = true)
	public String value() {
		return this.string;
	}
}
