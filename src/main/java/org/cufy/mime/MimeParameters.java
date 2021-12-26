/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.mime;

import org.cufy.internal.syntax.MimePattern;
import org.cufy.internal.syntax.MimeRegExp;
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
 * <b>Mappings</b> (No Encode)
 * <br>
 * The parameters part of a mime.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2022.12.26
 */
public class MimeParameters implements Cloneable, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 780843876397629883L;

	/**
	 * The parameters' values.
	 *
	 * @since 0.3.0 ~2021.12.26
	 */
	@NotNull
	protected Map<@NotNull String, @NotNull String> values;

	/**
	 * Construct a new parameters.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public MimeParameters() {
		this.values = new LinkedHashMap<>();
	}

	/**
	 * Construct a new parameters from combining the given {@code values} with the
	 * semicolon ";" as the delimiter.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param values the parameters values.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.3.0 ~2021.12.26
	 */
	public MimeParameters(@NotNull Map<@NotNull String, @NotNull String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.values = values;
	}

	/**
	 * Construct a new parameters with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new parameters.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2022.12.26
	 */
	public MimeParameters(@NotNull Consumer<@NotNull MimeParameters> builder) {
		Objects.requireNonNull(builder, "builder");
		this.values = new LinkedHashMap<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new parameters from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed parameters.
	 * @return a new parameters from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  MimeRegExp#PARAMETERS}.
	 * @since 0.3.0 ~2021.12.26
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static MimeParameters parse(@NotNull @Pattern(MimeRegExp.PARAMETERS) String source) {
		Objects.requireNonNull(source, "source");

		if (!MimePattern.PARAMETERS.matcher(source).matches())
			throw new IllegalArgumentException("invalid parameters: " + source);

		Map<String, String> values =
				Arrays.stream(source.split("\\;"))
					  .map(v -> v.split("\\=", 2))
					  .collect(Collectors.toMap(
							  v -> v[0],
							  v -> v.length == 2 ? v[1] : "",
							  (f, s) -> s,
							  LinkedHashMap::new
					  ));

		return new MimeParameters(values);
	}

	/**
	 * Capture this parameters into a new object.
	 *
	 * @return a clone of this parameters.
	 * @since 0.3.0 ~2021.12.26
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public MimeParameters clone() {
		try {
			MimeParameters clone = (MimeParameters) super.clone();
			clone.values = new HashMap<>(this.values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two parameters are equal when they are the same instance or have an equal {@link
	 * #values()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a parameters and equals this.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof MimeParameters) {
			MimeParameters parameters = (MimeParameters) object;

			return Objects.equals(this.values, parameters.values());
		}

		return false;
	}

	/**
	 * The hash code of a parameters is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this parameters.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.values.hashCode();
	}

	/**
	 * A string representation of this MimeParameters. Invoke to get the text representing
	 * this in a request.
	 * <br>
	 * Typically (plural separated by {@code ;}):
	 * <pre>
	 *     Attribute
	 * </pre>
	 * Example:
	 * <pre>
	 *     mime_parameters=boundary=----------something
	 * </pre>
	 *
	 * @return a string representation of this MimeParameters.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(MimeRegExp.PARAMETERS)
	@Override
	public String toString() {
		return this.values.entrySet()
						  .stream()
						  .map(entry -> entry.getKey() + "=" + entry.getValue())
						  .collect(Collectors.joining(";"));
	}

	/**
	 * Get the value assigned to the given {@code name}.
	 *
	 * @param name the name of the value to be returned. Or {@code null} if no such
	 *             value.
	 * @return the value assigned to the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  MimeRegExp#PARAMETER_NAME}.
	 * @since 0.3.0 ~2021.12.26
	 */
	@Nullable
	@Contract(pure = true)
	@Pattern(MimeRegExp.PARAMETER_VALUE)
	public String get(@NotNull @Pattern(MimeRegExp.PARAMETER_NAME) String name) {
		Objects.requireNonNull(name, "name");
		if (!MimePattern.PARAMETER_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid parameters value name: " + name);
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
	 *                                       {@link MimeRegExp#PARAMETER_NAME}; if the
	 *                                       given {@code value} does not match {@link
	 *                                       MimeRegExp#PARAMETER_VALUE}.
	 * @throws UnsupportedOperationException if this parameters is unmodifiable.
	 * @since 0.3.0 ~2021.12.26
	 */
	@Contract(mutates = "this")
	public void put(@NotNull @Pattern(MimeRegExp.PARAMETER_NAME) String name, @NotNull @Pattern(MimeRegExp.PARAMETER_VALUE) String value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		if (!MimePattern.PARAMETER_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid parameters value name: " + name);
		if (!MimePattern.PARAMETER_VALUE.matcher(value).matches())
			throw new IllegalArgumentException("invalid parameters value: " + value);

		this.values.put(name, value);
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link MimeRegExp#PARAMETER_NAME}.
	 * @throws UnsupportedOperationException if this parameters is unmodifiable.
	 * @since 0.3.0 ~2021.12.26
	 */
	@Contract(mutates = "this")
	public void remove(@NotNull @Pattern(MimeRegExp.PARAMETER_NAME) String name) {
		Objects.requireNonNull(name, "name");
		if (!MimePattern.PARAMETER_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid parameters value name: " + name);

		this.values.remove(name);
	}

	/**
	 * Return an unmodifiable view of the values of this parameters.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.3.0 ~2021.12.26
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	public Map<@NotNull String, @NotNull String> values() {
		return Collections.unmodifiableMap(this.values);
	}
}
