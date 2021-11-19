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
package org.cufy.http.impl;

import org.cufy.http.model.ReasonPhrase;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link ReasonPhrase}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class ReasonPhraseImpl implements ReasonPhrase {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8604270927744818959L;

	/**
	 * The reason-phrase literal.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Pattern(HttpRegExp.REASON_PHRASE)
	protected final String value;

	/**
	 * Construct a new default-implementation reason-phrase from the given {@code
	 * source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source to get the reason-phrase from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.20
	 */
	@ApiStatus.Internal
	public ReasonPhraseImpl(@NotNull @Pattern(HttpRegExp.REASON_PHRASE) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
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
	@Pattern(HttpRegExp.REASON_PHRASE)
	@Override
	public String toString() {
		return this.value;
	}
}
