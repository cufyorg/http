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

import org.cufy.http.internal.syntax.UriPattern;
import org.cufy.http.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <b>Mapping</b> (PCT Encode)
 * <br>
 * The "Query" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Query implements Cloneable, Serializable {
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
	 * Construct a new query.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Query() {
		this.values = new LinkedHashMap<>();
	}

	/**
	 * Construct a new query from combining the given {@code values} with the and-sign "&"
	 * as the delimiter.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param values the query values.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public Query(@NotNull Map<@NotNull String, @NotNull String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.values = values;
	}

	/**
	 * Construct a new query with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new query.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Query(@NotNull Consumer<@NotNull Query> builder) {
		Objects.requireNonNull(builder, "builder");
		this.values = new LinkedHashMap<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new query from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed query.
	 * @return a new query from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#QUERY}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Query parse(@NotNull @Pattern(UriRegExp.QUERY) String source) {
		Objects.requireNonNull(source, "source");

		if (!UriPattern.QUERY.matcher(source).matches())
			throw new IllegalArgumentException("invalid query: " + source);

		Map<String, String> values =
				Arrays.stream(source.split("\\&"))
					  .map(v -> v.split("\\=", 2))
					  .collect(Collectors.toMap(
							  v -> v[0],
							  v -> v.length == 2 ? v[1] : "",
							  (f, s) -> s,
							  LinkedHashMap::new
					  ));

		return new Query(values);
	}

	/**
	 * Capture this query into a new object.
	 *
	 * @return a clone of this query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Query clone() {
		try {
			Query clone = (Query) super.clone();
			clone.values = new HashMap<>(this.values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two queries are equal when they are the same instance or have an equal {@link
	 * #values()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a query and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Query) {
			Query query = (Query) object;

			return Objects.equals(this.values(), query.values());
		}

		return false;
	}

	/**
	 * The hash code of a query is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this query.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.values.hashCode();
	}

	/**
	 * A string representation of this Query. Invoke to get the text representing this in
	 * a request.
	 * <br>
	 * Typically (plural separated by {@code &}):
	 * <pre>
	 *     Attribute
	 * </pre>
	 * Example:
	 * <pre>
	 *     search_query=how+to+be+good&device=android&origin=cufy.org
	 * </pre>
	 *
	 * @return a string representation of this Query.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.QUERY)
	@Override
	public String toString() {
		return this.values.entrySet()
						  .stream()
						  .map(entry -> entry.getKey() + "=" + entry.getValue())
						  .collect(Collectors.joining("&"));
	}

	/**
	 * Get the value assigned to the given {@code name}.
	 *
	 * @param name the name of the value to be returned. Or {@code null} if no such
	 *             value.
	 * @return the value assigned to the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  UriRegExp#ATTR_NAME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Nullable
	@Contract(pure = true)
	@Pattern(UriRegExp.ATTR_VALUE)
	public String get(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		Objects.requireNonNull(name, "name");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);
		return this.values.get(name);
	}

	/**
	 * Set the value of the attribute with the given {@code name} to the given {@code
	 * value}.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @throws NullPointerException          if the given {@code name} or {@code value} is
	 *                                       null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this query is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void put(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, @NotNull @Pattern(UriRegExp.ATTR_VALUE) String value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);
		if (!UriPattern.ATTR_VALUE.matcher(value).matches())
			throw new IllegalArgumentException("invalid query value: " + value);

		this.values.put(name, value);
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}.
	 * @throws UnsupportedOperationException if this query is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void remove(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		Objects.requireNonNull(name, "name");
		if (!UriPattern.ATTR_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid query value name: " + name);

		this.values.remove(name);
	}

	/**
	 * Return an unmodifiable view of the values of this query.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	public Map<@NotNull String, @NotNull String> values() {
		return Collections.unmodifiableMap(this.values);
	}
}
