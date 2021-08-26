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
package org.cufy.http.response;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link ReasonPhrase}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawReasonPhrase implements ReasonPhrase {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8604270927744818959L;

	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw reason-phrase.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawReasonPhrase() {
		this.value = "";
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw reason-phrase with the given {@code value}.
	 *
	 * @param value the value of the constructed reason-phrase.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawReasonPhrase(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof ReasonPhrase) {
			ReasonPhrase reasonPhrase = (ReasonPhrase) object;

			return Objects.equals(this.value, reasonPhrase.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}
}
