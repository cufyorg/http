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

import org.cufy.http.model.Headers;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A basic implementation of the interface {@link Headers}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class HeadersImpl implements Headers {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3881735342601146869L;

	/**
	 * The headers.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Map<@NotNull String, @NotNull String> values;

	/**
	 * Construct a new headers from combining the given {@code values} with the crlf
	 * "\r\n" as the delimiter.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param values the headers values.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@ApiStatus.Internal
	public HeadersImpl(@NotNull Map<@NotNull String, @NotNull String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.values = values;
	}

	@NotNull
	@Override
	public HeadersImpl clone() {
		try {
			HeadersImpl clone = (HeadersImpl) super.clone();
			clone.values = new HashMap<>(this.values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Headers) {
			Headers headers = (Headers) object;

			return Objects.equals(this.values(), headers.values());
		}

		return false;
	}

	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Override
	public String get(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!HttpPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);
		return this.values.get(name);
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public Headers put(@NotNull String name, @NotNull String value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		if (!HttpPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);
		if (!HttpPattern.FIELD_VALUE.matcher(value).matches())
			throw new IllegalArgumentException("invalid field value: " + value);

		this.values.put(name, value);
		return this;
	}

	@NotNull
	@Override
	public Headers remove(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!HttpPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);

		this.values.remove(name);
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.HEADERS)
	@Override
	public String toString() {
		return this.values.entrySet()
						  .stream()
						  .map(entry -> entry.getKey() + ": " + entry.getValue() + "\r\n")
						  .collect(Collectors.joining());
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Map<@NotNull String, @NotNull String> values() {
		return Collections.unmodifiableMap(this.values);
	}
}
