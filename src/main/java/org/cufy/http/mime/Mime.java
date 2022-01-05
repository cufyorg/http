/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.mime;

import org.cufy.http.internal.syntax.MimeParse;
import org.cufy.http.internal.syntax.MimePattern;
import org.cufy.http.internal.syntax.MimeRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * <b>Components</b>
 * <br>
 * An object representing a mime.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link MimeType}</li>
 *     <li>{@link MimeSubtype}</li>
 *     <li>{@link MimeParameters}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2022.12.26
 */
public class Mime implements Serializable, Cloneable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4971161566517014753L;

	/**
	 * The parameters of this mime.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	protected MimeParameters parameters;
	/**
	 * The subtype.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Pattern(MimeRegExp.SUB_TYPE)
	protected String subtype;
	/**
	 * The type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Pattern(MimeRegExp.TYPE)
	protected String type;

	/**
	 * Construct a new media type.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Mime() {
		this.type = MimeType.TEXT;
		this.subtype = MimeSubtype.ANY;
		this.parameters = new MimeParameters();
	}

	/**
	 * Construct a media type with the given components.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param type    the mime main type.
	 * @param subtype the mime subtype.
	 * @throws NullPointerException if the given {@code type} or {@code subtype} is null.
	 * @since 0.3.0 ~2022.12.26
	 */
	public Mime(
			@NotNull @Pattern(MimeRegExp.TYPE) String type,
			@NotNull @Pattern(MimeRegExp.SUB_TYPE) String subtype
	) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(subtype, "subtype");
		this.type = type;
		this.subtype = subtype;
		this.parameters = new MimeParameters();
	}

	/**
	 * Construct a media type with the given components.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param type       the mime main type.
	 * @param subtype    the mime subtype.
	 * @param parameters the parameters part.
	 * @throws NullPointerException if the given {@code type} or {@code subtype} or {@code
	 *                              parameters} is null.
	 * @since 0.3.0 ~2022.12.26
	 */
	public Mime(
			@NotNull @Pattern(MimeRegExp.TYPE) String type,
			@NotNull @Pattern(MimeRegExp.SUB_TYPE) String subtype,
			@NotNull MimeParameters parameters
	) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(subtype, "subtype");
		Objects.requireNonNull(parameters, "mimeParameters");
		this.type = type;
		this.subtype = subtype;
		this.parameters = parameters;
	}

	/**
	 * Construct a new mime with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new mime.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2022.12.26
	 */
	public Mime(@NotNull Consumer<@NotNull Mime> builder) {
		Objects.requireNonNull(builder, "builder");
		this.type = MimeType.TEXT;
		this.subtype = MimeSubtype.ANY;
		this.parameters = new MimeParameters();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new default-implementation mime with its path literal being the given
	 * {@code source}.
	 *
	 * @param source the source of the mime literal of the constructed media-type.
	 * @return a new mime from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  MimeRegExp#MEDIA_TYPE}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Mime parse(@NotNull @Pattern(MimeRegExp.MEDIA_TYPE) String source) {
		Objects.requireNonNull(source, "source");
		if (!MimePattern.MEDIA_TYPE.matcher(source).matches())
			throw new IllegalArgumentException("invalid media type: " + source);

		Matcher matcher = MimeParse.MEDIA_TYPE.matcher(source);

		if (!matcher.find())
			throw new IllegalArgumentException("invalid media type: " + source);

		String mimeTypeSrc = matcher.group("Type");
		String mimeSubtypeSrc = matcher.group("Subtype");
		String mimeParametersSrc = matcher.group("Parameters");

		String type = mimeTypeSrc == null ?
					  MimeType.TEXT : MimeType.parse(mimeTypeSrc);
		String subtype = mimeSubtypeSrc == null ?
						 MimeSubtype.ANY : mimeSubtypeSrc;
		MimeParameters parameters =
				mimeParametersSrc == null ?
				new MimeParameters() :
				MimeParameters.parse(mimeParametersSrc);

		return new Mime(
				type,
				subtype,
				parameters
		);
	}

	/**
	 * Capture this mime into a new object.
	 *
	 * @return a clone of this mime.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Override
	@Contract(value = "->new", pure = true)
	public Mime clone() {
		try {
			Mime clone = (Mime) super.clone();
			clone.parameters = this.parameters.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two mimes are equal when their type, subtype and parameters are equal.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a media type and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Mime) {
			Mime mime = (Mime) object;

			return Objects.equals(this.type, mime.getMimeType()) &&
				   Objects.equals(this.subtype, mime.getMimeSubtype()) &&
				   Objects.equals(this.parameters, mime.getMimeParameters());
		}

		return false;
	}

	/**
	 * The hash code of a mime is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this mime.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.type.hashCode() ^
			   this.subtype.hashCode() ^
			   this.parameters.hashCode();
	}

	/**
	 * A string representation of this mime. Invoke to get the text representing this in a
	 * request.
	 *
	 * @return a string representation of this Mime.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Pattern(MimeRegExp.MEDIA_TYPE)
	@Contract(pure = true)
	@Override
	public String toString() {
		String type = this.type;
		String subtype = this.subtype;
		String parameters = this.parameters.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(type);
		builder.append("/");
		builder.append(subtype);

		if (!parameters.isEmpty())
			builder.append(";")
				   .append(parameters);

		return builder.toString();
	}

	/**
	 * Return the parameters list of this media-type.
	 *
	 * @return the parameters list of this.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(pure = true)
	public MimeParameters getMimeParameters() {
		return this.parameters;
	}

	/**
	 * Return the current mime subtype.
	 *
	 * @return the mime subtype of this.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Pattern(MimeRegExp.SUB_TYPE)
	@Contract(pure = true)
	public String getMimeSubtype() {
		return this.subtype;
	}

	/**
	 * Return the current mime type.
	 *
	 * @return the mime type of this.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Pattern(MimeRegExp.TYPE)
	@Contract(pure = true)
	public String getMimeType() {
		return this.type;
	}

	/**
	 * Set the mime parameters to be teh given {@code parameters}.
	 *
	 * @param parameters the parameters to be set.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if this mime does not allow changing its
	 *                                       parameters.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Contract(mutates = "this")
	public void setMimeParameters(@NotNull MimeParameters parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = parameters;
	}

	/**
	 * Set the mime subtype to be teh given {@code subtype}.
	 *
	 * @param subtype the subtype to be set.
	 * @throws NullPointerException          if the given {@code subtype} is null.
	 * @throws UnsupportedOperationException if this mime does not allow changing its
	 *                                       subtype.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Contract(mutates = "this")
	public void setMimeSubtype(@NotNull @Pattern(MimeRegExp.SUB_TYPE) String subtype) {
		Objects.requireNonNull(subtype, "subtype");
		this.subtype = subtype;
	}

	/**
	 * Set the mime type to be teh given {@code type}.
	 *
	 * @param type the type to be set.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if this mime does not allow changing its
	 *                                       type.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Contract(mutates = "this")
	public void setMimeType(@NotNull @Pattern(MimeRegExp.TYPE) String type) {
		Objects.requireNonNull(type, "type");
		this.type = type;
	}
}
