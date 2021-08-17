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

import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.Query;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.util.Map;
import java.util.Objects;
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
	 * Note: The constructed body will NOT have the {@link #contentType()} of the given
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
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its parameters set from the given {@code map}.
	 *
	 * @param map the map to set the values of constructed body from.
	 * @throws NullPointerException     if the given {@code map} is null.
	 * @throws IllegalArgumentException if a key in the given {@code map} does not match
	 *                                  {@link URIRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  URIRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	public ParametersBody(@NotNull Map<@Nullable @NonNls String, @Nullable @NonNls String> map) {
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
	 *                                  URIRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull @NonNls @Pattern(URIRegExp.QUERY) String source) {
		Objects.requireNonNull(source, "source");
		this.values = Query.query(source);
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new parameters-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #contentType()} of the given
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
	public static ParametersBody parameters(@NotNull Body body) {
		return new ParametersBody(body);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new parameters body.
	 *
	 * @return a new default parameters body.
	 * @since 0.0.6 ~2021.03.29
	 */
	public static ParametersBody parameters() {
		return new ParametersBody();
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
	 *                                  {@link URIRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  URIRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	public static ParametersBody parameters(@NotNull Map<@Nullable @NonNls String, @Nullable @NonNls String> map) {
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
	 *                                  URIRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	public static ParametersBody parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) String source) {
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
	public static ParametersBody parameters(@NotNull Query values) {
		return new ParametersBody(values);
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

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.FIELD_VALUE)
	@Override
	public String contentType() {
		return "application/x-www-form-urlencoded; charset=utf-8";
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

			return Objects.equals(this.contentType(), body.contentType()) &&
				   Objects.equals(this.toString(), object.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.values.hashCode();
	}

	@NotNull
	@NonNls
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
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody compute(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, UnaryOperator<@NonNls String> operator) {
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
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody computeIfAbsent(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, Supplier<@NonNls String> supplier) {
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
	 *                                       {@link URIRegExp#ATTR_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable and the
	 *                                       {@code operator} returned another value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody computeIfPresent(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, UnaryOperator<@NonNls String> operator) {
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
	 *                                  URIRegExp#ATTR_NAME}.
	 * @since 0.0.6 ~2021.03.31
	 */
	@Nullable
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.ATTR_VALUE)
	public String get(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name) {
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
	 *                                       {@link URIRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if query of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public ParametersBody put(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String value) {
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
	 *                                       {@link URIRegExp#ATTR_NAME}.
	 * @throws UnsupportedOperationException if the query of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody remove(@NotNull @NonNls @Pattern(URIRegExp.ATTR_NAME) String name) {
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
//
//	/**
//	 * Construct a new body with its parameters set from combining the given {@code
//	 * parameters}.
//	 *
//	 * @param parameters the parameters segments of the constructed body.
//	 * @throws NullPointerException     if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException if an element in the given {@code parameters} does
//	 *                                  not match {@link URIRegExp#ATTR_VALUE}.
//	 * @since 0.0.6 ~2021.03.29
//	 */
//	public ParametersBody(@Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
//		Objects.requireNonNull(parameters, "parameters");
//		this.value = Query.parse(parameters);
//	}
//
//	/**
//	 * Set the parameters of this to the product of combining the given {@code parameters}
//	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
//	 * parameters} array will be skipped.
//	 *
//	 * @param parameters the values of the new parameters of this.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
//	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
//	 * @throws UnsupportedOperationException if the parameters of this body cannot be
//	 *                                       changed.
//	 * @since 0.0.6 ~2021.03.29
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	public ParametersBody parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
//		return this.parameters(Query.parse(parameters));
//	}
//
//	/**
//	 * Set the parameters of this from the given {@code parameters}.
//	 *
//	 * @param parameters the parameters to be set.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws UnsupportedOperationException if the parameters of this body cannot be
//	 *                                       changed.
//	 * @since 0.0.6 ~2021.03.29
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	public ParametersBody parameters(@NotNull Query parameters) {
//		Objects.requireNonNull(parameters, "parameters");
//		this.value = parameters;
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this from the given {@code parameters} literal.
//	 *
//	 * @param parameters the parameters literal to set the parameters of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if the given {@code parameters} does not
//	 *                                       match {@link URIRegExp#QUERY}.
//	 * @throws UnsupportedOperationException if the parameters of this body cannot be
//	 *                                       changed.
//	 * @since 0.0.6 ~2021.03.29
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	public ParametersBody parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) String parameters) {
//		return this.parameters(Query.parse(parameters));
//	}
//
//	/**
//	 * Replace the parameters of this body to be the result of invoking the given {@code
//	 * operator} with the current parameters of this body. If the {@code operator}
//	 * returned null then nothing happens.
//	 * <br>
//	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
//	 *
//	 * @param operator the computing operator.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code operator} is null.
//	 * @throws UnsupportedOperationException if the parameters of this body cannot be
//	 *                                       changed and the returned parameters from the
//	 *                                       given {@code operator} is different from the
//	 *                                       current parameters.
//	 * @since 0.0.6 ~2021.03.29
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	public ParametersBody parameters(@NotNull UnaryOperator<Query> operator) {
//		Objects.requireNonNull(operator, "operator");
//		Query p = this.parameters();
//		Query parameters = operator.apply(p);
//
//		if (parameters != null && parameters != p)
//			this.parameters(parameters);
//
//		return this;
//	}
