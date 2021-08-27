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
package org.cufy.http.uri;

import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A basic implementation of the interface {@link Query}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class AbstractQuery implements Query {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7977922092705435710L;

	/**
	 * The query values.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Map<@NotNull String, @NotNull String> values;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default query.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractQuery() {
		this.values = new HashMap<>();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new query from copying the given {@code query}.
	 *
	 * @param query the query to copy.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractQuery(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.values = new HashMap<>(query.values());
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new query from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed query.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#QUERY}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractQuery(@NotNull @Pattern(UriRegExp.QUERY) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.QUERY.matcher(source).matches())
			throw new IllegalArgumentException("invalid query: " + source);
		this.values = Arrays.stream(source.split("\\&"))
							.map(v -> v.split("\\=", 2))
							.collect(Collectors.toMap(
									v -> v[0],
									v -> v.length == 2 ? v[1] : "",
									(f, s) -> s,
									HashMap::new
							));
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new query from combining the given {@code values} with the and-sign "&"
	 * as the delimiter. The null keys or values in the given {@code source} will be
	 * treated as it does not exist.
	 *
	 * @param values the query values.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if a key in the given {@code values} does not
	 *                                  match {@link UriRegExp#ATTR_NAME}; if a value in
	 *                                  the given {@code values} does not match {@link
	 *                                  UriRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractQuery(@NotNull Map<@Nullable String, @Nullable String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection SimplifyStreamApiCallChains
		this.values = StreamSupport.stream(values.entrySet().spliterator(), false)
								   .filter(e -> e != null && e.getKey() != null &&
												e.getValue() != null)
								   .collect(Collectors.toMap(
										   e -> {
											   String name = e.getKey();

											   if (!UriPattern.ATTR_NAME.matcher(name).matches())
												   throw new IllegalArgumentException(
														   "invalid query value name: " +
														   name);

											   return name;
										   },
										   e -> {
											   String value = e.getValue();

											   assert value != null;

											   if (!UriPattern.ATTR_VALUE.matcher(value).matches())
												   throw new IllegalArgumentException(
														   "invalid query value: " +
														   value);

											   return value;
										   },
										   (f, s) -> s,
										   HashMap::new
								   ));
	}

	@NotNull
	@Override
	public AbstractQuery clone() {
		try {
			AbstractQuery clone = (AbstractQuery) super.clone();
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
		if (object instanceof Query) {
			Query query = (Query) object;

			//noinspection ObjectInstantiationInEqualsHashCode
			return Objects.equals(this.values(), query.values());
		}

		return false;
	}

	@Nullable
	@Pattern(UriRegExp.ATTR_VALUE)
	@Override
	public String get(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);
		return this.values.get(name);
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public Query put(@NotNull String name, @NotNull String value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);
		if (!UriPattern.ATTR_VALUE.matcher(value).matches())
			throw new IllegalArgumentException("invalid query value: " + value);

		this.values.put(name, value);
		return this;
	}

	@NotNull
	@Override
	public Query remove(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);

		this.values.remove(name);
		return this;
	}

	@NotNull

	@Pattern(UriRegExp.QUERY)
	@Override
	public String toString() {
		return this.values.entrySet()
						  .stream()
						  .map(entry -> entry.getKey() + "=" + entry.getValue())
						  .collect(Collectors.joining("&"));
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Map<@NotNull String, @NotNull String> values() {
		return Collections.unmodifiableMap(this.values);
	}
}
