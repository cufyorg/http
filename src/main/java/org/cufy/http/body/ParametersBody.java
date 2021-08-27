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
package org.cufy.http.body;

import org.cufy.http.syntax.HttpRegExp;
import org.cufy.http.syntax.UriRegExp;
import org.cufy.http.uri.Query;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * <b>Mapping</b>
 * <br>
 * A body implementation that follows the {@code application/x-www-form-urlencoded}
 * content type.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.29
 */
public class ParametersBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4568284231778109146L;

	/**
	 * The request in-body parameters. (if any)
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	protected Query values;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new parameters body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody() {
		this.values = Query.query();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new parameters-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #getContentType()} of the given
	 * {@code body} and might not have the exact content. (the content might get
	 * reformatted, rearranged, compressed, encoded or encrypted/decrypted)
	 *
	 * @param body the body to be copied.
	 * @throws NullPointerException     if the given {@code body} is null.
	 * @throws IllegalArgumentException if the given {@code body} cannot be converted into
	 *                                  a parameters body.
	 * @since 0.0.1 ~2021.03.30
	 */
	public ParametersBody(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		this.values = Query.query(body.toString());
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its parameters set from the given {@code map}.
	 *
	 * @param map the map to set the values of constructed body from.
	 * @throws NullPointerException     if the given {@code map} is null.
	 * @throws IllegalArgumentException if a key in the given {@code map} does not match
	 *                                  {@link UriRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  UriRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	public ParametersBody(@NotNull Map<@Nullable String, @Nullable String> map) {
		Objects.requireNonNull(map, "map");
		this.values = Query.query(map);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new body with its value set from the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull @Pattern(UriRegExp.QUERY) String source) {
		Objects.requireNonNull(source, "source");
		this.values = Query.query(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body with its values set to the given {@code values}.
	 *
	 * @param values the values of the constructed body.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull Query values) {
		Objects.requireNonNull(values, "values");
		this.values = Query.query(values);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new parameters body.
	 *
	 * @return a new default parameters body.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static ParametersBody parameters() {
		return new ParametersBody();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new parameters-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #getContentType()} of the given
	 * {@code body} and might not have the exact content. (the content might get
	 * reformatted, rearranged, compressed, encoded or encrypted/decrypted)
	 *
	 * @param body the body to be copied.
	 * @return a new parameters body from copying the given {@code body}.
	 * @throws NullPointerException     if the given {@code body} is null.
	 * @throws IllegalArgumentException if the given {@code body} cannot be converted into
	 *                                  a parameters body.
	 * @since 0.0.1 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parameters(@NotNull Body body) {
		return new ParametersBody(body);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its parameters set from the given {@code map}.
	 *
	 * @param map the map to set the values of constructed body from.
	 * @return a new parameters body from the given {@code map}.
	 * @throws NullPointerException     if the given {@code map} is null.
	 * @throws IllegalArgumentException if a key in the given {@code map} does not match
	 *                                  {@link UriRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  UriRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parameters(@NotNull Map<@Nullable String, @Nullable String> map) {
		return new ParametersBody(map);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new body with its value set from the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @return a new parameters body from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parameters(@NotNull @Pattern(UriRegExp.QUERY) String source) {
		return new ParametersBody(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body with its values set to the given {@code values}.
	 *
	 * @param values the values of the constructed body.
	 * @return a new parameters body with the given components.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parameters(@NotNull Query values) {
		return new ParametersBody(values);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new parameters body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new parameters body.
	 * @return the parameters body constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parameters(@NotNull Consumer<ParametersBody> builder) {
		Objects.requireNonNull(builder, "builder");
		ParametersBody parametersBody = new ParametersBody();
		builder.accept(parametersBody);
		return parametersBody;
	}

	@NotNull
	@Override
	public ParametersBody clone() {
		try {
			ParametersBody clone = (ParametersBody) super.clone();
			clone.values = this.values.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof ParametersBody) {
			ParametersBody body = (ParametersBody) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.values, body.values);
		}
		if (object instanceof Body) {
			Body body = (Body) object;

			return Objects.equals(this.getContentType(), body.getContentType()) &&
				   Objects.equals(this.toString(), object.toString());
		}

		return false;
	}

	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getContentLength() {
		return this.toString()
				   .codePoints()
				   .map(cp -> cp <= 0x7ff ? cp <= 0x7f ? 1 : 2 : cp <= 0xffff ? 3 : 4)
				   .asLongStream()
				   .sum();
	}

	@NotNull
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Override
	public String getContentType() {
		return "application/x-www-form-urlencoded; charset=utf-8";
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public String toString() {
		return this.values.toString();
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
	 *                                       {@link UriRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody compute(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, UnaryOperator<String> operator) {
		this.values.compute(name, operator);
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
	 *                                       {@link UriRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody computeIfAbsent(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, Supplier<String> supplier) {
		this.values.computeIfAbsent(name, supplier);
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
	 *                                       {@link UriRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody computeIfPresent(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, UnaryOperator<String> operator) {
		this.values.computeIfPresent(name, operator);
		return this;
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
	 * @since 0.0.6 ~2021.03.31
	 */
	@Nullable
	@Contract(pure = true)
	@Pattern(UriRegExp.ATTR_VALUE)
	public String get(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		return this.values.get(name);
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
	 *                                       {@link UriRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if query of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody put(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, @NotNull @Pattern(UriRegExp.ATTR_VALUE) String value) {
		this.values.put(name, value);
		return this;
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody remove(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		this.values.remove(name);
		return this;
	}

	/**
	 * Return the parameters defined for this.
	 *
	 * @return the parameters of this.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "->new", pure = true)
	public Query values() {
		return Query.raw(this.values);
	}
}
