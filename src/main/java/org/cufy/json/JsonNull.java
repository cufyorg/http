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

/**
 * Json null constant.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
public class JsonNull implements JsonElement {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2702120651023068480L;

	/**
	 * Construct a new json null.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@ApiStatus.Internal
	public JsonNull() {
	}

	@NotNull
	@Override
	public JsonNull clone() {
		try {
			return (JsonNull) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@NotNull Object object) {
		return object instanceof JsonNull;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@NotNull
	@Override
	public String json() {
		return "null";
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		return "null";
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}
}
