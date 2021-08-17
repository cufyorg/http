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

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * <b>Mapping</b> (PCT Encode)
 * <br>
 * The "Query" part of an URI.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Query extends Cloneable, Serializable {
	/**
	 * An empty query constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Query EMPTY = new RawQuery();

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new query from copying the given {@code query}.
	 *
	 * @param query the query to copy.
	 * @return a new copy of the given {@code query}.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Query query(@NotNull Query query) {
		return new AbstractQuery(query);
	}

	/**
	 * Decode the given {@code value} to be used.
	 *
	 * @param value the value to be decoded.
	 * @return the decoded value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(pure = true)
	static String decode(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String value) {
		Objects.requireNonNull(value, "value");
		try {
			//noinspection deprecation
			return URLDecoder.decode(value);
		} catch (Throwable e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Encode the given {@code value} to be sent.
	 *
	 * @param value the value to be encoded.
	 * @return the encoded value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.ATTR_VALUE)
	static String encode(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		try {
			//noinspection deprecation
			return URLEncoder.encode(value);
		} catch (Throwable e) {
			throw new InternalError(e);
		}
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new query instance to be a placeholder if a the user has not specified a
	 * query.
	 *
	 * @return a new default query.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Query query() {
		return new AbstractQuery();
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new query from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed query.
	 * @return a new query from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#QUERY}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Query query(@NotNull @NonNls @Pattern(URIRegExp.QUERY) String source) {
		return new AbstractQuery(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw query with the given {@code value}.
	 *
	 * @param value the value of the constructed query.
	 * @return a new raw query.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Query raw(@NotNull @NonNls String value) {
		return new RawQuery(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code query}.
	 *
	 * @param query the query to be copied.
	 * @return an unmodifiable copy of the given {@code query}.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Query raw(@NotNull Query query) {
		return new RawQuery(query);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new query from combining the given {@code values} with the and-sign "&"
	 * as the delimiter. The null keys or values in the given {@code values} will be
	 * treated as it does not exist.
	 *
	 * @param values the query values.
	 * @return a new query from parsing and joining the given {@code values}.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if a key in the given {@code values} does not
	 *                                  match {@link URIRegExp#ATTR_NAME}; if a value in
	 *                                  the given {@code values} does not match {@link
	 *                                  URIRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Query query(@NotNull Map<@Nullable @NonNls String, @Nullable @NonNls String> values) {
		return new AbstractQuery(values);
	}

	/**
	 * Set the value of the given {@code name} to be the results of invoking the given
	 * {@code operator} with the first argument being the current value assigned to the
	 * given {@code name} or an empty string if currently it is not set. If the {@code
	 * operator} returned {@code null} then the value with the given {@code name} will be
	 * removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code operator}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this query is unmodifiable and the {@code
	 *                                       operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Query compute(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		String v = this.get(name);

		if (v == null) {
			String value = operator.apply("");

			if (value != null)
				this.put(name, value);
		} else {
			String value = operator.apply(v);

			if (value == null)
				this.remove(name);
			else if (!value.equals(v))
				this.put(name, value);
		}

		return this;
	}

	/**
	 * If absent, set the value of the given {@code name} to be the results of invoking
	 * the given {@code supplier}. If the {@code supplier} returned {@code null} nothing
	 * happens.
	 * <br>
	 * Throwable thrown by the {@code supplier} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param supplier the computing supplier.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code supplier}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this query is unmodifiable and the {@code
	 *                                       operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Query computeIfAbsent(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, Supplier<@NonNls String> supplier) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(supplier, "supplier");
		String v = this.get(name);

		if (v == null) {
			String value = supplier.get();

			if (value != null)
				this.put(name, value);
		}

		return this;
	}

	/**
	 * If present, set the value of the given {@code name} to be the results of invoking
	 * the given {@code operator} with the first argument being the current value assigned
	 * to the given {@code name}. If the {@code operator} returned {@code null} then the
	 * value with the given {@code name} will be removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code operator}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this query is unmodifiable and the {@code
	 *                                       operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Query computeIfPresent(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		String v = this.get(name);

		if (v != null) {
			String value = operator.apply(v);

			if (value == null)
				this.remove(name);
			else if (!value.equals(v))
				this.put(name, value);
		}

		return this;
	}

	/**
	 * Set the value of the attribute with the given {@code name} to the given {@code
	 * value}.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code value} is
	 *                                       null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link URIRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this query is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Query put(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String value) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link URIRegExp#ATTR_NAME}.
	 * @throws UnsupportedOperationException if this query is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Query remove(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name) {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * Capture this query into a new object.
	 *
	 * @return a clone of this query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Query clone();

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
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a query is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this query.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.QUERY)
	@Override
	String toString();

	/**
	 * Get the value assigned to the given {@code name}.
	 *
	 * @param name the name of the value to be returned. Or {@code null} if no such
	 *             value.
	 * @return the value assigned to the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  URIRegExp#ATTR_NAME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Nullable
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.ATTR_VALUE)
	String get(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name);

	/**
	 * Return an unmodifiable view of the values of this query.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "->new", pure = true)
	Map<@NotNull @NonNls String, @NotNull @NonNls String> values();
}
//
//	/**
//	 * Construct a new query from combining the given {@code values} with the and-sign "&"
//	 * as the delimiter. The null elements are skipped.
//	 *
//	 * @param values the query values.
//	 * @return a new query from parsing and joining the given {@code values}.
//	 * @throws NullPointerException     if the given {@code values} is null.
//	 * @throws IllegalArgumentException if an element in the given {@code values} does not
//	 *                                  match {@link URIRegExp#ATTR_VALUE}.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	static Query parse(@Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... values) {
//		return new AbstractQuery(values);
//	}
