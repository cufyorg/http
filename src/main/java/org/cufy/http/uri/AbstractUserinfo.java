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

import org.cufy.http.syntax.URIPattern;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A basic implementation of the interface {@link Userinfo}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractUserinfo implements Userinfo {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2945446570665191118L;

	/**
	 * The userinfo values.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected List<@NotNull @NonNls String> values;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default userinfo.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUserinfo() {
		this.values = new ArrayList<>();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new userinfo from copying the given {@code userinfo}.
	 *
	 * @param userinfo the userinfo to copy.
	 * @throws NullPointerException if the given {@code userinfo} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUserinfo(@NotNull Userinfo userinfo) {
		Objects.requireNonNull(userinfo, "userinfo");
		this.values = new ArrayList<>(userinfo.values());
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new userinfo from combining the given {@code values} with the colon ":"
	 * as the delimiter. The null elements in the given {@code values} will be treated as
	 * it does not exist.
	 *
	 * @param values the userinfo values.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link URIRegExp#USERINFO_NC}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserinfo(@NotNull List<@Nullable @NonNls String> values) {
		Objects.requireNonNull(values, "values");
		//noinspection SimplifyStreamApiCallChains
		this.values = StreamSupport.stream(values.spliterator(), false)
				.filter(Objects::nonNull)
				.peek(value -> {
					if (!URIPattern.USERINFO_NC.matcher(value).matches())
						throw new IllegalArgumentException(
								"invalid userinfo value: " + value);
				})
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new userinfo from parsing the given {@code source}.
	 *
	 * @param source the source to be parsed.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#USERINFO}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserinfo(@NotNull @NonNls @Pattern(URIRegExp.USERINFO) String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.USERINFO.matcher(source).matches())
			throw new IllegalArgumentException("invalid userinfo: " + source);
		this.values = new ArrayList<>(Arrays.asList(source.split("\\:")));
	}

	@NotNull
	@Override
	public AbstractUserinfo clone() {
		try {
			AbstractUserinfo clone = (AbstractUserinfo) super.clone();
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
		if (object instanceof Userinfo) {
			Userinfo userinfo = (Userinfo) object;

			//noinspection ObjectInstantiationInEqualsHashCode
			return Objects.equals(this.values(), userinfo.values());
		}

		return false;
	}

	@Nullable
	@NonNls
	@Pattern(URIRegExp.USERINFO_NC)
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
	public Userinfo put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull @NonNls String value) {
		Objects.requireNonNull(value, "value");
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		if (!URIPattern.USERINFO_NC.matcher(value).matches())
			throw new IllegalArgumentException("invalid userinfo value: " + value);

		int size = this.values.size();

		this.values.addAll(Collections.nCopies(Math.max(0, index + 1 - size), ""));
		this.values.set(index, value);

		return this;
	}

	@NotNull
	@Override
	public Userinfo remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");

		int size = this.values.size();

		this.values.subList(Math.min(index, size), size).clear();

		return this;
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.USERINFO)
	@Override
	public String toString() {
		return String.join(":", this.values);
	}

	@NotNull
	@UnmodifiableView
	@Override
	public List<@NotNull @NonNls String> values() {
		return Collections.unmodifiableList(this.values);
	}
}
