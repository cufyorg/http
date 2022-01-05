/*
 *	Copyright 2021-2022 Cufy
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
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * <b>Mappings</b> (PCT Encode)
 * <br>
 * The "Userinfo" part of the "Authority" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class UserInfo implements Cloneable, Serializable {
	/**
	 * Password userinfo index.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final int PASSWORD = 1;
	/**
	 * Username userinfo index.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final int USERNAME = 0;

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2945446570665191118L;

	/**
	 * The user info values.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected List<@NotNull String> values;

	/**
	 * Construct a new user info.
	 *
	 * @since 0.3.0 ~2021.03.21
	 */
	public UserInfo() {
		this.values = new LinkedList<>();
	}

	/**
	 * Construct a new user info from combining the given {@code values} with the colon
	 * ":" as the delimiter.
	 *
	 * @param values the user info values.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public UserInfo(@NotNull List<@NotNull String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.values = values;
	}

	/**
	 * Construct a new user info with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new user info.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public UserInfo(@NotNull Consumer<@NotNull UserInfo> builder) {
		Objects.requireNonNull(builder, "builder");
		this.values = new LinkedList<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
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
	public static UserInfo parse(@NotNull @Pattern(UriRegExp.USERINFO) String source) {
		Objects.requireNonNull(source, "source");

		if (!UriPattern.USERINFO.matcher(source).matches())
			throw new IllegalArgumentException("invalid user info: " + source);

		List<String> values = new ArrayList<>(Arrays.asList(source.split("\\:")));

		return new UserInfo(values);
	}

	/**
	 * Capture the content of this user info into a new independent user info instance.
	 *
	 * @return a clone of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public UserInfo clone() {
		try {
			UserInfo clone = (UserInfo) super.clone();
			clone.values = new ArrayList<>(this.values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

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
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) object;

			return Objects.equals(this.values(), userInfo.values());
		}

		return false;
	}

	/**
	 * The hash code of a user info is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this user info.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.values.hashCode();
	}

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
	public String toString() {
		return String.join(":", this.values);
	}

	/**
	 * Add the given {@code value} to the end of the userinfo.
	 *
	 * @param value the value to be added.
	 * @throws NullPointerException          if the given {@code value} is null.
	 * @throws IllegalArgumentException      if the given {@code value} does not match
	 *                                       {@link UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(mutates = "this")
	public void add(@NotNull @Pattern(UriRegExp.USERINFO_NC) String value) {
		Objects.requireNonNull(value, "value");
		if (!UriPattern.USERINFO_NC.matcher(value).matches())
			throw new IllegalArgumentException("invalid user info value: " + value);

		this.values.add(value);
	}

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
	public String get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		return index < this.values.size() ? this.values.get(index) : null;
	}

	/**
	 * Set the {@code index}-th attribute's value to be the given {@code value}.
	 *
	 * @param index the index of the attribute.
	 * @param value the value to be set.
	 * @throws NullPointerException          if the given {@code value} is null.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the given {@code value} does not match {@link
	 *                                       UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if this user info is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull @Pattern(UriRegExp.USERINFO_NC) String value) {
		Objects.requireNonNull(value, "value");
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		if (!UriPattern.USERINFO_NC.matcher(value).matches())
			throw new IllegalArgumentException("invalid user info value: " + value);

		int size = this.values.size();

		this.values.addAll(Collections.nCopies(Math.max(0, index + 1 - size), ""));
		this.values.set(index, value);
	}

	/**
	 * Remove the {@code index}-th attribute and the attributes after it.
	 *
	 * @param index the index of the attribute.
	 * @throws IllegalArgumentException      if the given {@code index} is negative.
	 * @throws UnsupportedOperationException if this user info is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");

		int size = this.values.size();

		this.values.subList(Math.min(index, size), size).clear();
	}

	/**
	 * Return an unmodifiable view of the values of this user info.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	public List<@NotNull String> values() {
		return Collections.unmodifiableList(this.values);
	}
}
