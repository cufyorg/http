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

import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A basic implementation of the interface {@link UserInfo}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractUserInfo implements UserInfo {
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
	 * <b>Default</b>
	 * <br>
	 * Construct a new default user info.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUserInfo() {
		this.values = new ArrayList<>();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new userInfo from copying the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to copy.
	 * @throws NullPointerException if the given {@code userInfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUserInfo(@NotNull UserInfo userInfo) {
		Objects.requireNonNull(userInfo, "userInfo");
		this.values = new ArrayList<>(userInfo.values());
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new user info from parsing the given {@code source}.
	 *
	 * @param source the source to be parsed.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#USERINFO}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserInfo(@NotNull @Pattern(UriRegExp.USERINFO) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.USERINFO.matcher(source).matches())
			throw new IllegalArgumentException("invalid user info: " + source);
		this.values = new ArrayList<>(Arrays.asList(source.split("\\:")));
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new user info from combining the given {@code values} with the colon ":"
	 * as the delimiter. The null elements in the given {@code values} will be treated as
	 * it does not exist.
	 *
	 * @param values the user info values.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link UriRegExp#USERINFO_NC}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserInfo(@NotNull List<@Nullable String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection SimplifyStreamApiCallChains
		this.values = StreamSupport.stream(values.spliterator(), false)
								   .filter(Objects::nonNull)
								   .peek(value -> {
									   if (!UriPattern.USERINFO_NC.matcher(value).matches())
										   throw new IllegalArgumentException(
												   "invalid user info value: " + value);
								   })
								   .collect(Collectors.toCollection(ArrayList::new));
	}

	@NotNull
	@Override
	public AbstractUserInfo clone() {
		try {
			AbstractUserInfo clone = (AbstractUserInfo) super.clone();
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

			//noinspection ObjectInstantiationInEqualsHashCode
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
		//noinspection NonFinalFieldReferencedInHashCode
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
