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

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A raw implementation of the interface {@link UserInfo}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawUserInfo implements UserInfo {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -7396167396693917224L;

	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;
	/**
	 * The list to be returned by {@link #values()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final List<@NotNull String> values;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw user info.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUserInfo() {
		this.value = "";
		this.values = Collections.emptyList();
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to be copied.
	 * @throws NullPointerException if the given {@code userInfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUserInfo(@NotNull UserInfo userInfo) {
		Objects.requireNonNull(userInfo, "userInfo");
		this.value = userInfo.toString();
		this.values = Collections.unmodifiableList(new ArrayList<>(userInfo.values()));
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw user info with the given {@code value}.
	 *
	 * @param value the value of the constructed user info.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUserInfo(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.values = Collections.emptyList();
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw user info with the given {@code value}.
	 *
	 * @param value  the value of the constructed user info.
	 * @param values the list to be returned by {@link #values()}.
	 * @throws NullPointerException if the given {@code value} or {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUserInfo(@NotNull String value, List<@NotNull String> values) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(values, "values");
		this.value = value;
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
	}

	@NotNull
	@Override
	public RawUserInfo clone() {
		try {
			return (RawUserInfo) super.clone();
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

			//noinspection ObjectInstantiationInEqualsHashCode
			return Objects.equals(this.values, userInfo.values());
		}

		return false;
	}

	@Nullable
	@Pattern(".*")
	@Override
	public String get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		return index < this.values.size() ? this.values.get(index) : null;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public List<@NotNull String> values() {
		return this.values;
	}
}
