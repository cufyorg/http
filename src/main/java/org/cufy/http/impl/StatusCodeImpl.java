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

import org.cufy.http.model.StatusCode;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link StatusCode}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class StatusCodeImpl implements StatusCode {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1575494331266689670L;

	/**
	 * The status-code literal.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Pattern(HttpRegExp.STATUS_CODE)
	protected final String value;

	/**
	 * Construct a new default-implementation status-code from the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source to get the status number from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.20
	 */
	@ApiStatus.Internal
	public StatusCodeImpl(@NotNull @Pattern(HttpRegExp.STATUS_CODE) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof StatusCode) {
			StatusCode statusCode = (StatusCode) object;

			return Objects.equals(this.value, statusCode.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(HttpRegExp.STATUS_CODE)
	@Override
	public String toString() {
		return this.value;
	}
}
