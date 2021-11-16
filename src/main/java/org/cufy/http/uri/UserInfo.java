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

import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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
	 * Decode the given {@code value} to be used.
	 *
	 * @param value the value to be decoded.
	 * @return the decoded value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(pure = true)
	static String decode(@NotNull @Pattern(UriRegExp.USERINFO_NC) String value) {
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
	@Contract(pure = true)
	@Pattern(UriRegExp.USERINFO_NC)
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
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw user info with the given {@code value}.
	 *
	 * @param value the value of the constructed user info.
	 * @return a new raw user info.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo raw(@NotNull String value) {
		return new RawUserInfo(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code userInfo}.
	 *
	 * @param userInfo the user info to be copied.
	 * @return an unmodifiable copy of the given {@code userInfo}.
	 * @throws NullPointerException if the given {@code userInfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo raw(@NotNull UserInfo userInfo) {
		return new RawUserInfo(userInfo);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new user info instance to be a placeholder if a the user has not specified
	 * a user info.
	 *
	 * @return a new default user info.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static UserInfo userInfo() {
		return new AbstractUserInfo();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new userInfo from copying the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to copy.
	 * @return a new copy of the given {@code userInfo}.
	 * @throws NullPointerException if the given {@code userInfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo userInfo(@NotNull UserInfo userInfo) {
		return new AbstractUserInfo(userInfo);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Create a new user info from parsing the given {@code source}.
	 *
	 * @param source the user info sequence to be parsed into a new user info.
	 * @return a user info from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#USERINFO}.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo userInfo(@NotNull @Pattern(UriRegExp.USERINFO) String source) {
		return new AbstractUserInfo(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new user info from combining the given {@code values} with the colon
	 * ":" as the delimiter. The null elements in the given {@code values} will be treated
	 * as it does not exist.
	 *
	 * @param values the user info values.
	 * @return a new user info from parsing and joining the given {@code values}.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link UriRegExp#USERINFO_NC}.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo userInfo(@NotNull List<@Nullable String> values) {
		return new AbstractUserInfo(values);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new user info with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new user info.
	 * @return the user info constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static UserInfo userInfo(@NotNull Consumer<UserInfo> builder) {
		Objects.requireNonNull(builder, "builder");
		UserInfo userInfo = new AbstractUserInfo();
		builder.accept(userInfo);
		return userInfo;
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
			@Subst("admin") String value = operator.apply(v);

			if (value == null)
				this.remove(index);
			else if (!value.equals(v))
				this.put(index, value);
		}

		return this;
	}

	/**
	 * Invoke the given {@code operator} with {@code this} as the parameter and return the
	 * result returned from the operator.
	 *
	 * @param operator the operator to be invoked.
	 * @return the result from invoking the given {@code operator} or {@code this} if the
	 * 		given {@code operator} returned {@code null}.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->new")
	default UserInfo map(UnaryOperator<UserInfo> operator) {
		Objects.requireNonNull(operator, "operator");
		UserInfo mapped = operator.apply(this);
		return mapped == null ? this : mapped;
	}

	/**
	 * Execute the given {@code consumer} with {@code this} as the parameter.
	 *
	 * @param consumer the consumer to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code consumer} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->this")
	default UserInfo peek(Consumer<UserInfo> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		consumer.accept(this);
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
