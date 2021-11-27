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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Json string constant class.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
public class JsonString implements JsonElement {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8423875023841804519L;

	/**
	 * The value of this string.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	public final String string;

	/**
	 * Construct a new json string.
	 *
	 * @param string the string to be the string of the constructed string.
	 * @since 0.3.0 ~2021.11.23
	 */
	@ApiStatus.Internal
	public JsonString(@NotNull String string) {
		Objects.requireNonNull(string, "string");
		this.string = string;
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
}
