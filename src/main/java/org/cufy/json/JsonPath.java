package org.cufy.json;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A dot-based json path implementation.
 * <br>
 * This is a really simple json path implementation. MIGHT CHANGE IN THE FUTURE TO
 * <a href="https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html">Json
 * Path</a>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonPath implements Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -399118804854558425L;

	/**
	 * The index of the segment this path is representing.
	 *
	 * @since 0.3.0 ~2021.11.27
	 */
	protected final int index;
	/**
	 * The segments of this path.
	 *
	 * @since 0.3.0 ~2021.11.27
	 */
	@NotNull
	protected final List<Segment> segments;

	/**
	 * Construct a new json path with the given components.
	 *
	 * @param segments the segments the constructed path will be having.
	 * @param index    the index of the segment the constructed path is representing.
	 * @throws NullPointerException      if the given {@code segments} is null.
	 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >=
	 *                                   segments.size()}.
	 * @since 0.3.0 ~2021.11.27
	 */
	@ApiStatus.Internal
	public JsonPath(@NotNull List<@NotNull Segment> segments, int index) {
		Objects.requireNonNull(segments, "segments");
		if (index < 0 || index >= segments.size())
			throw new IndexOutOfBoundsException(
					"index: " + index + " size: " + segments.size()
			);
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.segments = segments;
		this.index = index;
	}

	/**
	 * Construct a new json path from parsing the given {@code source}.
	 *
	 * @param source the string to be parsed into a json path.
	 * @return a new json path from parsing the given string.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonPath parse(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		List<Segment> segments = new ArrayList<>();

		//noinspection DynamicRegexReplaceableByCompiledPattern
		for (String split : source.split("[.]")) {
			boolean optional = split.endsWith("?");
			boolean lenient = optional && split.endsWith("??");
			String name = split
					.substring(0, split.length() - (lenient ? 2 : optional ? 1 : 0));

			segments.add(new Segment(
					name,
					optional,
					lenient
			));
		}

		return new JsonPath(segments, 0);
	}

	/**
	 * Two paths are always equal when they are the same object or has the same {@link
	 * #index} and an equal {@link #segments}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a path and equals this.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonPath) {
			JsonPath path = (JsonPath) object;

			return this.index == path.index &&
				   Objects.equals(this.segments, path.segments);
		}

		return false;
	}

	/**
	 * The hash code of a json path is its index {@code xor} the hash code of its
	 * segments.
	 *
	 * @return the hash code of this path.
	 * @since 0.3.0 ~2021.11.27
	 */
	@Contract(pure = true)
	@Override
	public int hashCode() {
		return this.index ^ this.segments.hashCode();
	}

	/**
	 * The string representation of a json path is its segments combined.
	 *
	 * @return a string representation of this path.
	 * @since 0.3.0 ~2021.11.27
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		List<String> list = new ArrayList<>();

		JsonPath path = this;

		while (path != null) {
			String name = path.getName().replace(".", "\\.");

			if (path.isOptional())
				name += "?";
			if (path.isLenient())
				name += "?";

			list.add(0, name);

			path = path.getPrevious();
		}

		return String.join(".", list);
	}

	/**
	 * Return the name of the last token in this path.
	 *
	 * @return the name of this path.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(pure = true)
	public String getName() {
		return this.segments.get(this.index).getName();
	}

	/**
	 * Return the next path.
	 *
	 * @return the next path.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(pure = true)
	public JsonPath getNext() {
		return this.index + 1 >= this.segments.size() ? null :
			   new JsonPath(this.segments, this.index + 1);
	}

	/**
	 * Return the previous path.
	 *
	 * @return the previous path.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(pure = true)
	public JsonPath getPrevious() {
		return this.index <= 0 ? null :
			   new JsonPath(this.segments, this.index - 1);
	}

	/**
	 * Return true if this path is forgiving type mismatches and inaccessible properties.
	 *
	 * @return true, if this path is lenient.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(pure = true)
	public boolean isLenient() {
		return this.segments.get(this.index).isLenient();
	}

	/**
	 * Return true if the last token of this path is optional.
	 *
	 * @return true, if this path is optional.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(pure = true)
	public boolean isOptional() {
		return this.segments.get(this.index).isOptional();
	}

	/**
	 * A class representing a segment (part) in a json path.
	 *
	 * @author LSafer
	 * @version 0.3.0
	 * @since 0.3.0 ~2021.11.27
	 */
	public static class Segment implements Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -4018659758750791024L;

		/**
		 * True, if this part is lenient.
		 *
		 * @since 0.3.0 ~2021.11.27
		 */
		protected final boolean lenient;
		/**
		 * The name of this part.
		 *
		 * @since 0.3.0 ~2021.11.27
		 */
		protected final String name;
		/**
		 * True, if this part is optional.
		 *
		 * @since 0.3.0 ~2021.11.27
		 */
		protected final boolean optional;

		/**
		 * Construct a new json path part.
		 *
		 * @param name     the name of the constructed part.
		 * @param optional true, to make the constructed part optional.
		 * @param lenient  true, to make the constructed part lenient.
		 * @throws NullPointerException if the given {@code name} is null.
		 * @since 0.3.0 ~2021.11.27
		 */
		protected Segment(@NotNull String name, boolean optional, boolean lenient) {
			Objects.requireNonNull(name, "name");
			this.name = name;
			this.optional = optional;
			this.lenient = lenient;
		}

		/**
		 * The hash code of a segment is the hash code of its name {@code xor} the boolean
		 * hash code of its optionality {@code xor} the boolean hash code of its {@code
		 * lenient} field.
		 *
		 * @return the hash code of this segment.
		 * @since 0.3.0 ~2021.11.27
		 */
		@Contract(pure = true)
		@Override
		public int hashCode() {
			return this.name.hashCode() ^
				   Boolean.hashCode(this.optional) ^
				   Boolean.hashCode(this.lenient);
		}

		/**
		 * Two segments are equal when they are the same object or have an equal {@link
		 * #name}, {@link #optional} and {@link #lenient} values.
		 *
		 * @param object the object to be checked.
		 * @return if the given {@code object} is a segment and equals this.
		 * @since 0.3.0 ~2021.11.27
		 */
		@Contract(pure = true)
		@Override
		public boolean equals(@Nullable Object object) {
			if (object == this)
				return true;
			if (object instanceof Segment) {
				Segment segment = (Segment) object;

				return this.optional == segment.optional &&
					   this.lenient == segment.lenient &&
					   Objects.equals(this.name, segment.name);
			}

			return false;
		}

		/**
		 * The string representation of a segment is the string representation of its
		 * components combined.
		 *
		 * @return a string representation of this segment.
		 * @since 0.3.0 ~2021.11.27
		 */
		@NotNull
		@Override
		public String toString() {
			return this.name +
				   (this.optional ? "?" : "") +
				   (this.lenient ? "?" : "");
		}

		/**
		 * Return the name of this part.
		 *
		 * @return the name of this part.
		 * @since 0.3.0 ~2021.11.27
		 */
		@NotNull
		public String getName() {
			return this.name;
		}

		/**
		 * Return true if this path part is lenient.
		 *
		 * @return true, if this part is lenient.
		 * @since 0.3.0 ~2021.11.27
		 */
		@Contract(pure = true)
		public boolean isLenient() {
			return this.lenient;
		}

		/**
		 * Return true if this path part is optional.
		 *
		 * @return true, if this part is optional.
		 * @since 0.3.0 ~2021.11.27
		 */
		@Contract(pure = true)
		public boolean isOptional() {
			return this.optional;
		}
	}
}
