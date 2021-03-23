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
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.util.*;

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
	protected ArrayList<@NotNull @NonNls String> values = new ArrayList<>();

	/**
	 * Construct a new empty userinfo.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserinfo() {

	}

	/**
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
		this.values.addAll(Arrays.asList(source.split("\\:")));
	}

	/**
	 * Construct a new userinfo from combining the given {@code values} with the colon ":"
	 * as the delimiter. The null elements in the given {@code source} will be treated as
	 * empty strings.
	 *
	 * @param values the userinfo values.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link URIRegExp#USERINFO_NC}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUserinfo(@Nullable @NonNls @Pattern(URIRegExp.USERINFO_NC) String @NotNull ... values) {
		Objects.requireNonNull(values, "values");
		for (String value : values)
			if (value == null)
				this.values.add("");
			else if (!URIPattern.USERINFO_NC.matcher(value).matches())
				throw new IllegalArgumentException("invalid userinfo value: " + value);
			else
				this.values.add(value);
	}

	@Override
	public AbstractUserinfo clone() {
		try {
			AbstractUserinfo clone = (AbstractUserinfo) super.clone();
			clone.values = (ArrayList<String>) this.values.clone();
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
		@Subst("admin") String s =
				index < this.values.size() ? this.values.get(index) : null;
		return s;
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
		@Subst("admin:admin") String s = String.join(":", this.values);
		return s;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public List<@NotNull @NonNls String> values() {
		return Collections.unmodifiableList(this.values);
	}
}
