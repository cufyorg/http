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
package org.cufy.http.model;

import org.cufy.http.raw.RawUserInfo;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
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
 * The "Userinfo" part of the "Authority" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface UserInfo extends Cloneable, Serializable {
	/**
	 * An empty user info constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	UserInfo EMPTY = new RawUserInfo();

	/**
	 * Password userinfo index.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	int PASSWORD = 1;
	/**
	 * Username userinfo index.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	int USERNAME = 0;

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
	 *                                       UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default UserInfo compute(@Range(from = 0, to = Integer.MAX_VALUE) int index, UnaryOperator<String> operator) {
		Objects.requireNonNull(operator, "operator");
		String v = this.get(index);

		if (v == null) {
			String value = operator.apply("");

			if (value != null)
				this.put(index, value);
		} else {
			String value = operator.apply(v);

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
	 *                                       does not match {@link UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default UserInfo computeIfAbsent(@Range(from = 0, to = Integer.MAX_VALUE) int index, Supplier<String> supplier) {
		Objects.requireNonNull(supplier, "supplier");
		String v = this.get(index);

		if (v == null) {
			String value = supplier.get();

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
	 *                                       UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable and the
	 *                                       given {@code operator} returned another
	 *                                       value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default UserInfo computeIfPresent(@Range(from = 0, to = Integer.MAX_VALUE) int index, UnaryOperator<String> operator) {
		Objects.requireNonNull(operator, "operator");
		String v = this.get(index);

		if (v != null) {
			String value = operator.apply(v);

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
	 *                                       UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default UserInfo put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull @Pattern(UriRegExp.USERINFO_NC) String value) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * Remove the {@code index}-th attribute and the attributes after it.
	 *
	 * @param index the index of the attribute.
	 * @return this.
	 * @throws IllegalArgumentException      if the given {@code index} is negative.
	 * @throws UnsupportedOperationException if this user info is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default UserInfo remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * Capture the content of this user info into a new independent user info instance.
	 *
	 * @return a clone of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	UserInfo clone();

	/**
	 * Two user info are equal when they are the same instance or have an equal {@link
	 * #values()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a user info and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a user info is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this user info.
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
	@Contract(pure = true)
	@Pattern(UriRegExp.USERINFO)
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
	@Contract(pure = true)
	@Pattern(UriRegExp.USERINFO_NC)
	String get(@Range(from = 0, to = Integer.MAX_VALUE) int index);

	/**
	 * Return an unmodifiable view of the values of this user info.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	List<@NotNull String> values();
}
