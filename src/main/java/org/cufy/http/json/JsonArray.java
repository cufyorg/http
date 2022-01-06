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
package org.cufy.http.json;

import org.cufy.http.json.token.JsonArrayToken;
import org.cufy.http.json.token.JsonTokenException;
import org.cufy.http.json.token.JsonTokenSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.Collections.nCopies;

/**
 * A json array is an object that can contain other elements in an ordered list.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonArray implements JsonStruct, List<@NotNull JsonElement> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8752738627046789535L;

	/**
	 * The array backing this object.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	protected List<@NotNull JsonElement> list;

	/**
	 * Construct a new json array.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonArray() {
		this.list = new LinkedList<>();
	}

	/**
	 * Construct a new json array.
	 *
	 * @param list the list to be backing the constructed json array.
	 * @throws NullPointerException if the given {@code list} is null.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonArray(@NotNull List<@NotNull JsonElement> list) {
		Objects.requireNonNull(list, "list");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.list = list;
	}

	/**
	 * Construct a new json array with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new json array.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public JsonArray(@NotNull Consumer<JsonArray> builder) {
		Objects.requireNonNull(builder, "builder");
		this.list = new LinkedList<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new json array from parsing the given {@code source}.
	 *
	 * @param source the source string to be parsed.
	 * @return a new json array from parsing the given source.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is invalid json array.
	 * @since 0.3.0 ~2021.12.15
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonArray parse(@NotNull @Language("json") String source) {
		Objects.requireNonNull(source, "source");
		try {
			return new JsonArrayToken(new JsonTokenSource(new StringReader(source)))
					.nextElement();
		} catch (JsonTokenException e) {
			throw new IllegalArgumentException(e.formatMessage(source), e);
		} catch (IOException e) {
			throw new InternalError(e);
		}
	}

	@Nullable
	@Override
	public JsonElement assign(@NotNull JsonPath path, @NotNull JsonElement element) {
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(element, "element");
		String name = path.getName();
		JsonPath previous = path.getPrevious();
		JsonPath next = path.getNext();

		//noinspection DynamicRegexReplaceableByCompiledPattern
		if (!name.matches("\\d+")) {
			if (previous != null && previous.isLenient())
				return null;

			throw new IllegalArgumentException(
					"Invalid array index " + path
			);
		}

		int index = Integer.parseInt(name);
		int length = this.size();

		if (next != null) {
			JsonElement e = index < length ? this.get(index) : null;

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.assign(next, element);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non json struct " + next
			);
		}

		if (index >= length)
			this.addAll(nCopies(index - length + 1, Json.NULL));

		return this.set(index, element);
	}

	/**
	 * Capture this element into a new object.
	 *
	 * @return a clone of this element.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Override
	public JsonArray clone() {
		try {
			JsonArray clone = (JsonArray) super.clone();
			clone.list = new LinkedList<>(this.list);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Nullable
	@Override
	public JsonElement delete(@NotNull JsonPath path) {
		Objects.requireNonNull(path, "path");
		String name = path.getName();
		JsonPath previous = path.getPrevious();
		JsonPath next = path.getNext();

		//noinspection DynamicRegexReplaceableByCompiledPattern
		if (!name.matches("\\d+")) {
			if (previous != null && previous.isLenient())
				return null;

			throw new IllegalArgumentException(
					"Invalid array index " + path
			);
		}

		int index = Integer.parseInt(name);
		int length = this.size();

		if (next != null) {
			JsonElement e = index < length ? this.get(index) : null;

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.delete(next);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non json struct " + next
			);
		}

		return index < length ? this.remove(index) : null;
	}

	@NotNull
	@Override
	public String json() {
		StringBuilder builder = new StringBuilder();

		builder.append("[");

		Iterator<JsonElement> iterator = this.iterator();

		if (iterator.hasNext())
			while (true) {
				JsonElement next = iterator.next();

				builder.append(next.json());

				if (!iterator.hasNext())
					break;

				builder.append(",");
			}

		builder.append("]");

		return builder.toString();
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		Objects.requireNonNull(indent, "indent");
		Objects.requireNonNull(tab, "tab");
		String indentTab = indent + tab;
		StringBuilder builder = new StringBuilder();

		builder.append("[");

		Iterator<JsonElement> iterator = this.iterator();

		if (iterator.hasNext())
			while (true) {
				JsonElement next = iterator.next();

				builder.append("\n");
				builder.append(next.json(indentTab, tab));

				if (!iterator.hasNext())
					break;

				builder.append(",");
			}

		builder.append("]");

		return builder.toString();
	}

	@Nullable
	@Override
	public JsonElement query(@NotNull JsonPath path) {
		Objects.requireNonNull(path, "path");
		String name = path.getName();
		JsonPath previous = path.getPrevious();
		JsonPath next = path.getNext();

		//noinspection DynamicRegexReplaceableByCompiledPattern
		if (!name.matches("\\d+")) {
			if (previous != null && previous.isLenient())
				return null;

			throw new IllegalArgumentException(
					"Invalid array index " + path
			);
		}

		int index = Integer.parseInt(name);
		int length = this.size();

		if (next != null) {
			JsonElement e = index < length ? this.get(index) : null;

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.query(next);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non struct property " + next
			);
		}

		return index < length ? this.get(index) : null;
	}

	@NotNull
	@Override
	public JsonArray subList(int fromIndex, int toIndex) {
		return new JsonArray(this.list.subList(fromIndex, toIndex));
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}

	// delegate

	@Override
	public boolean add(@NotNull JsonElement element) {
		return this.list.add(element);
	}

	@Override
	public void add(int index, @NotNull JsonElement element) {
		this.list.add(index, element);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends @NotNull JsonElement> collection) {
		return this.list.addAll(collection);
	}

	@Override
	public boolean addAll(int index, @NotNull Collection<? extends @NotNull JsonElement> collection) {
		return this.list.addAll(index, collection);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean contains(@Nullable Object object) {
		return this.list.contains(object);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> collection) {
		return this.list.containsAll(collection);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object object) {
		return this == object || this.list.equals(object);
	}

	@Override
	public void forEach(@NotNull Consumer<? super @NotNull JsonElement> action) {
		this.list.forEach(action);
	}

	@NotNull
	@Override
	public JsonElement get(int index) {
		return this.list.get(index);
	}

	@Override
	public int hashCode() {
		return this.list.hashCode();
	}

	@Override
	public int indexOf(@Nullable Object object) {
		return this.list.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@NotNull
	@Override
	public Iterator<@NotNull JsonElement> iterator() {
		return this.list.iterator();
	}

	@Override
	public int lastIndexOf(@Nullable Object object) {
		return this.list.lastIndexOf(object);
	}

	@NotNull
	@Override
	public ListIterator<@NotNull JsonElement> listIterator() {
		return this.list.listIterator();
	}

	@NotNull
	@Override
	public ListIterator<@NotNull JsonElement> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public Stream<@NotNull JsonElement> parallelStream() {
		return this.list.parallelStream();
	}

	@Override
	public boolean remove(@Nullable Object object) {
		return this.list.remove(object);
	}

	@NotNull
	@Override
	public JsonElement remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		return this.list.removeAll(collection);
	}

	@Override
	public boolean removeIf(@NotNull Predicate<? super @NotNull JsonElement> filter) {
		return this.list.removeIf(filter);
	}

	@Override
	public void replaceAll(@NotNull UnaryOperator<@NotNull JsonElement> operator) {
		this.list.replaceAll(operator);
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		return this.list.retainAll(collection);
	}

	@NotNull
	@Override
	public JsonElement set(int index, @NotNull JsonElement element) {
		return this.list.set(index, element);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void sort(Comparator<? super @NotNull JsonElement> comparator) {
		this.list.sort(comparator);
	}

	@NotNull
	@Override
	public Spliterator<@NotNull JsonElement> spliterator() {
		return this.list.spliterator();
	}

	@NotNull
	@Override
	public Stream<@NotNull JsonElement> stream() {
		return this.list.stream();
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@NotNull
	@Override
	public <T> T[] toArray(@NotNull T[] array) {
		return this.list.toArray(array);
	}

//	@Override
//	public <T> T[] toArray(@NotNull IntFunction<T[]> generator) {
//		return this.list.toArray(generator);
//	}
}
