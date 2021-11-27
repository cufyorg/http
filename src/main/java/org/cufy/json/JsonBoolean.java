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

/**
 * Json boolean constants.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
public class JsonBoolean implements JsonElement {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5765100584515821750L;

	/**
	 * The value of this boolean.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	protected final boolean value;

	/**
	 * Construct a new json boolean.
	 *
	 * @param value the value to be the value of the constructed boolean.
	 * @since 0.3.0 ~2021.11.23
	 */
	@ApiStatus.Internal
	public JsonBoolean(boolean value) {
		this.value = value;
	}

	@NotNull
	@Override
	public JsonBoolean clone() {
		try {
			return (JsonBoolean) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonBoolean) {
			JsonBoolean bool = (JsonBoolean) object;

			return this.value == bool.value;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(this.value);
	}

	@NotNull
	@Override
	public String json() {
		return this.value ? "true" : "false";
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		return this.value ? "true" : "false";
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}
}
