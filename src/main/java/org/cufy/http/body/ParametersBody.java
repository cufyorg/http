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

import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.Query;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
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
	protected Query parameters = Query.defaultQuery();

	/**
	 * Construct a new parameters body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody() {

	}

	/**
	 * Construct a new body with its parameters set from the given {@code parameters}.
	 *
	 * @param parameters the parameters of the constructed body.
	 * @throws NullPointerException     if the given {@code parameters} is null.
	 * @throws IllegalArgumentException if the given {@code parameters} does not match
	 *                                  {@link URIRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=parameters") String parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = Query.parse(parameters);
	}

	/**
	 * Construct a new body with its parameters set from combining the given {@code
	 * parameters}.
	 *
	 * @param parameters the parameters segments of the constructed body.
	 * @throws NullPointerException     if the given {@code parameters} is null.
	 * @throws IllegalArgumentException if an element in the given {@code parameters} does
	 *                                  not match {@link URIRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = Query.parse(parameters);
	}

	/**
	 * Construct a new body with its parameters set to the given {@code parameters}.
	 *
	 * @param parameters the parameters of the constructed body.
	 * @throws NullPointerException if the given {@code parameters} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull Query parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = parameters;
	}

	@NotNull
	@Override
	public ParametersBody clone() {
		try {
			ParametersBody clone = (ParametersBody) super.clone();
			clone.parameters = this.parameters.clone();
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
			return Objects.equals(this.parameters, body.parameters);
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.parameters.hashCode();
	}

	@Range(from = 0, to = Long.MAX_VALUE)
	@Override
	public long length() {
		return this.parameters.toString().length();
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return this.parameters.toString();
	}

	/**
	 * Set the parameters of this from the given {@code parameters} literal.
	 *
	 * @param parameters the parameters literal to set the parameters of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
		return this.parameters(Query.parse(parameters));
	}

	/**
	 * Set the parameters of this to the product of combining the given {@code parameters}
	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
	 * parameters} array will be skipped.
	 *
	 * @param parameters the values of the new parameters of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		return this.parameters(Query.parse(parameters));
	}

	/**
	 * Set the parameters of this from the given {@code parameters}.
	 *
	 * @param parameters the parameters to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody parameters(@NotNull Query parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = parameters;
		return this;
	}

	/**
	 * Replace the parameters of this body to be the result of invoking the given {@code
	 * operator} with the current parameters of this body. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed and the returned parameters from the
	 *                                       given {@code operator} is different from the
	 *                                       current parameters.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public ParametersBody parameters(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Query p = this.parameters();
		Query parameters = operator.apply(p);

		if (parameters != null && parameters != p)
			this.parameters(parameters);

		return this;
	}

	/**
	 * Return the parameters defined for this.
	 *
	 * @return the parameters of this.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(pure = true)
	public Query parameters() {
		return this.parameters;
	}
}
