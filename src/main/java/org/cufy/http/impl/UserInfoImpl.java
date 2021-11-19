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
package org.cufy.http.impl;

import org.cufy.http.model.UserInfo;
import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link UserInfo}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class UserInfoImpl implements UserInfo {
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
	 * Construct a new user info from combining the given {@code values} with the colon
	 * ":" as the delimiter.
	 *
	 * @param values the user info values.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	@ApiStatus.Internal
	public UserInfoImpl(@NotNull List<@NotNull String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.values = values;
	}

	@NotNull
	@Override
	public UserInfo add(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		if (!UriPattern.USERINFO_NC.matcher(value).matches())
			throw new IllegalArgumentException("invalid user info value: " + value);

		this.values.add(value);

		return this;
	}

	@NotNull
	@Override
	public UserInfoImpl clone() {
		try {
			UserInfoImpl clone = (UserInfoImpl) super.clone();
			clone.values = new ArrayList<>(this.values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) object;

			return Objects.equals(this.values(), userInfo.values());
		}

		return false;
	}

	@Nullable
	@Pattern(UriRegExp.USERINFO_NC)
	@Override
	public String get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		return index < this.values.size() ? this.values.get(index) : null;
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public UserInfo put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull String value) {
		Objects.requireNonNull(value, "value");
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		if (!UriPattern.USERINFO_NC.matcher(value).matches())
			throw new IllegalArgumentException("invalid user info value: " + value);

		int size = this.values.size();

		this.values.addAll(Collections.nCopies(Math.max(0, index + 1 - size), ""));
		this.values.set(index, value);

		return this;
	}

	@NotNull
	@Override
	public UserInfo remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");

		int size = this.values.size();

		this.values.subList(Math.min(index, size), size).clear();

		return this;
	}

	@NotNull
	@Pattern(UriRegExp.USERINFO)
	@Override
	public String toString() {
		return String.join(":", this.values);
	}

	@NotNull
	@UnmodifiableView
	@Override
	public List<@NotNull String> values() {
		return Collections.unmodifiableList(this.values);
	}
}
