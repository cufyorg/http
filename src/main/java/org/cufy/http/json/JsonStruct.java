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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A json struct is an object that can contain other elements.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public interface JsonStruct extends JsonElement {
	/**
	 * Invoke the given {@code operator} with the current element at the given {@code
	 * path}. Then, if the result of invoking the operator is null, remove the element.
	 * Otherwise, update the element to the result.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param path     the to update its element.
	 * @param operator the operator to be invoked.
	 * @return the element at the given {@code path} after the execution of this function.
	 * @throws NullPointerException     if the given {@code path} or {@code operator} is
	 *                                  null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws NoSuchElementException   if one of the middle elements does not exist.
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(mutates = "this")
	@ApiStatus.Experimental
	default JsonElement update(@NotNull JsonPath path, @NotNull UnaryOperator<@Nullable JsonElement> operator) {
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(operator, "operator");
		JsonElement e = this.query(path);
		JsonElement element = operator.apply(e);

		if (element != e)
			if (element == null) {
				this.delete(path);
				return null;
			} else {
				this.assign(path, element);
				return element;
			}

		return e;
	}

	/**
	 * Capture this struct into a new object.
	 *
	 * @return a clone of this struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	JsonStruct clone();

	/**
	 * Assign the given {@code element} to the given {@code path} down the hierarchy of
	 * this struct.
	 *
	 * @param path    the path to assign the element to.
	 * @param element the element to be assigned.
	 * @return the previous element at the given {@code path}. Or {@code null} if none.
	 * @throws NullPointerException     if the given {@code path} or {@code element} is
	 *                                  null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws NoSuchElementException   if one of the middle elements does not exist.
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(mutates = "this")
	@ApiStatus.Experimental
	JsonElement assign(@NotNull JsonPath path, @NotNull JsonElement element);

	/**
	 * Remove the element at the given {@code path} down the hierarchy of this struct.
	 *
	 * @param path the path to be removed.
	 * @return the previous element at the given {@code path}. Or {@code null} if none.
	 * @throws NullPointerException     if the given {@code path} is null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(mutates = "this")
	@ApiStatus.Experimental
	JsonElement delete(@NotNull JsonPath path);

	/**
	 * Return the element at the given {@code path} down the hierarchy of this struct.
	 *
	 * @param path the path to return the element of.
	 * @return the element at the given {@code path}.
	 * @throws NullPointerException     if the given {@code path} is null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(pure = true)
	@ApiStatus.Experimental
	JsonElement query(@NotNull JsonPath path);
}
