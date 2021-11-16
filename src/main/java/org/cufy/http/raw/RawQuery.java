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

import org.cufy.http.model.Query;
import org.cufy.http.syntax.UriPattern;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A raw implementation of the interface {@link Query}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawQuery implements Query {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7977922092705435710L;

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
	 * Construct a new empty raw query.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawQuery() {
		this.value = "";
		this.values = Collections.emptyMap();
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code query}.
	 *
	 * @param query the query to be copied.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawQuery(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.value = query.toString();
		this.values = Collections.unmodifiableMap(new HashMap<>(query.values()));
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw query with the given {@code value}.
	 *
	 * @param value the value of the constructed query.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawQuery(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.values = Collections.emptyMap();
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw query with the given {@code value}.
	 *
	 * @param value  the value of the constructed query.
	 * @param values the map to be returned by {@link #values()}.
	 * @throws NullPointerException if the given {@code value} or {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawQuery(@NotNull String value, Map<@NotNull String, @NotNull String> values) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(values, "values");
		this.value = value;
		this.values = Collections.unmodifiableMap(new HashMap<>(values));
	}

	@NotNull
	@Override
	public RawQuery clone() {
		try {
			return (RawQuery) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Query) {
			Query query = (Query) object;

			return Objects.equals(this.values, query.values());
		}

		return false;
	}

	@Nullable
	@Pattern(".*")
	@Override
	public String get(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);
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
