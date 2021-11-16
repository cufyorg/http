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
package org.cufy.http.raw;

import org.cufy.http.model.Headers;
import org.cufy.http.syntax.HttpPattern;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A raw implementation of the interface {@link Headers}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawHeaders implements Headers {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3881735342601146869L;

	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;
	/**
	 * The map to be returned by {@link #values()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Map<@NotNull String, @NotNull String> values;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw headers.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawHeaders() {
		this.value = "";
		this.values = Collections.emptyMap();
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code headers}.
	 *
	 * @param headers the headers to be copied.
	 * @throws NullPointerException if the given {@code headers} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawHeaders(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.value = headers.toString();
		this.values = Collections.unmodifiableMap(new HashMap<>(headers.values()));
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw headers with the given {@code value}.
	 *
	 * @param value the value of the constructed headers.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawHeaders(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.values = Collections.emptyMap();
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw headers with the given {@code value}.
	 *
	 * @param value  the value of the constructed headers.
	 * @param values the map to be returned by {@link #values()}.
	 * @throws NullPointerException if the given {@code value} or {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawHeaders(@NotNull String value, Map<@NotNull String, @NotNull String> values) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(values, "values");
		this.value = value;
		this.values = Collections.unmodifiableMap(new HashMap<>(values));
	}

	@NotNull
	@Override
	public RawHeaders clone() {
		try {
			return (RawHeaders) super.clone();
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

			return Objects.equals(this.values, headers.values());
		}

		return false;
	}

	@Nullable
	@Pattern(".*")
	@Override
	public String get(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!HttpPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);
		return this.values.get(name);
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

	@NotNull
	@UnmodifiableView
	@Override
	public Map<@NotNull String, @NotNull String> values() {
		return this.values;
	}
}
