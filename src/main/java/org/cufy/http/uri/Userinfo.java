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
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * <b>Mappings</b> (PCT Encode)
 * <br>
 * The "Userinfo" part of the "Authority" part of an URI.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Userinfo extends Cloneable, Serializable {
	/**
	 * An empty userinfo constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Userinfo EMPTY = new RawUserinfo();

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new userinfo from copying the given {@code userinfo}.
	 *
	 * @param userinfo the userinfo to copy.
	 * @return a new copy of the given {@code userinfo}.
	 * @throws NullPointerException if the given {@code userinfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Userinfo userinfo(@NotNull Userinfo userinfo) {
		return new AbstractUserinfo(userinfo);
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
	static String decode(@NotNull @NonNls @Pattern(URIRegExp.USERINFO_NC) String value) {
		Objects.requireNonNull(value, "value");
		try {
			//noinspection deprecation
			return URLDecoder.decode(value);
		} catch (Throwable e) {
			throw new InternalError(e);
		}
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new userinfo instance to be a placeholder if a the user has not specified
	 * a userinfo.
	 *
	 * @return an new default userinfo.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Userinfo userinfo() {
		return new AbstractUserinfo();
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
	@Pattern(URIRegExp.USERINFO_NC)
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
	 * <b>Parse</b>
	 * <br>
	 * Create a new userinfo from parsing the given {@code source}.
	 *
	 * @param source the userinfo sequence to be parsed into a new userinfo.
	 * @return an userinfo from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#USERINFO}.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Userinfo userinfo(@NotNull @NonNls @Pattern(URIRegExp.USERINFO) String source) {
		return new AbstractUserinfo(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw userinfo with the given {@code value}.
	 *
	 * @param value the value of the constructed userinfo.
	 * @return a new raw userinfo.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Userinfo raw(@NotNull @NonNls String value) {
		return new RawUserinfo(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code userinfo}.
	 *
	 * @param userinfo the userinfo to be copied.
	 * @return an unmodifiable copy of the given {@code userinfo}.
	 * @throws NullPointerException if the given {@code userinfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Userinfo raw(@NotNull Userinfo userinfo) {
		return new RawUserinfo(userinfo);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new userinfo from combining the given {@code values} with the colon ":"
	 * as the delimiter. The null elements in the given {@code values} will be treated as
	 * it does not exist.
	 *
	 * @param values the userinfo values.
	 * @return a new userinfo from parsing and joining the given {@code values}.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link URIRegExp#USERINFO_NC}.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Userinfo userinfo(@NotNull List<@Nullable @NonNls String> values) {
		return new AbstractUserinfo(values);
	}

	/**
	 * Set the value at the given {@code index} to the results of invoking the given
	 * {@code operator} with the first argument being the current value at the given
	 * {@code index} or an empty string if currently it is not set. If the {@code
	 * operator} returned {@code null} then the value at {@code index} and all the values
	 * after it will be removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param index    the index of the value to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the string returned by the operator is not
	 *                                       {@code null} neither match {@link
	 *                                       URIRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this userinfo is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Userinfo compute(@Range(from = 0, to = Integer.MAX_VALUE) int index, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(operator, "operator");
		String v = this.get(index);

		if (v == null) {
			@Subst("admin") String value = operator.apply("");

			if (value != null)
				this.put(index, value);
		} else {
			@Subst("admin") String value = operator.apply(v);

			if (value == null)
				this.remove(index);
			else if (!value.equals(v))
				this.put(index, value);
		}

		return this;
	}

	/**
	 * If absent, set the value at the given {@code index} to be the results of invoking
	 * the given {@code supplier}. If the {@code supplier} returned {@code null} nothing
	 * happens.
	 * <br>
	 * Throwable thrown by the {@code supplier} will fall throw this method unhandled.
	 *
	 * @param index    the index of the value to be computed.
	 * @param supplier the computing supplier.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code supplier}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the value returned from the {@code operator}
	 *                                       does not match {@link URIRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this userinfo is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Userinfo computeIfAbsent(@Range(from = 0, to = Integer.MAX_VALUE) int index, Supplier<@NonNls String> supplier) {
		Objects.requireNonNull(supplier, "supplier");
		String v = this.get(index);

		if (v == null) {
			@Subst("admin") String value = supplier.get();

			if (value != null)
				this.put(index, value);
		}

		return this;
	}

	/**
	 * If present, set the value at the given {@code index} to the results of invoking the
	 * given {@code operator} with the first argument being the current value at the given
	 * {@code index}. If the {@code operator} returned {@code null} then the value at
	 * {@code index} and all the values after it will be removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param index    the index of the value to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the string returned by the operator is not
	 *                                       {@code null} neither match {@link
	 *                                       URIRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this userinfo is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Userinfo computeIfPresent(@Range(from = 0, to = Integer.MAX_VALUE) int index, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(operator, "operator");
		String v = this.get(index);

		if (v != null) {
			@Subst("admin") String value = operator.apply(v);

			if (value == null)
				this.remove(index);
			else if (!value.equals(v))
				this.put(index, value);
		}

		return this;
	}

	/**
	 * Set the {@code index}-th attribute's value to be the given {@code value}.
	 *
	 * @param index the index of the attribute.
	 * @param value the value to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code value} is null.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the given {@code value} does not match {@link
	 *                                       URIRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this userinfo is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Userinfo put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull @NonNls @Pattern(URIRegExp.USERINFO_NC) String value) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * Remove the {@code index}-th attribute and the attributes after it.
	 *
	 * @param index the index of the attribute.
	 * @return this.
	 * @throws IllegalArgumentException      if the given {@code index} is negative.
	 * @throws UnsupportedOperationException if this userinfo is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Userinfo remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * Capture the content of this userinfo into a new independent userinfo instance.
	 *
	 * @return a clone of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Userinfo clone();

	/**
	 * Two userinfo are equal when they are the same instance or have an equal {@link
	 * #values()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a userinfo and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of an userinfo is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this userinfo.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Userinfo. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Typically (plural delimiter ":"):
	 * <pre>
	 *     value
	 * </pre>
	 * Example:
	 * <pre>
	 *     username:password
	 * </pre>
	 *
	 * @return a string representation of this Userinfo.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.USERINFO)
	@Override
	String toString();

	/**
	 * Get the value at the given {@code index}.
	 *
	 * @param index the index of the value to be returned.
	 * @return the value at the given {@code index}. Or {@code null} if no such value.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Nullable
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.USERINFO_NC)
	String get(@Range(from = 0, to = Integer.MAX_VALUE) int index);

	/**
	 * Return an unmodifiable view of the values of this userinfo.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "->new", pure = true)
	List<@NotNull @NonNls String> values();
}
