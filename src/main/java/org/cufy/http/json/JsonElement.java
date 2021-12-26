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
package org.cufy.http.json;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A json element is an object that can be translated to json.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public interface JsonElement extends Cloneable, Serializable {
	/**
	 * Capture this element into a new object.
	 *
	 * @return a clone of this element.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	JsonElement clone();

	/**
	 * A string representation of this json element.
	 * <br>
	 * Always returns a valid json string.
	 *
	 * @return a string representation of this json element.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Language("json")
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * A string representation of this json element.
	 * <br>
	 * Always returns a valid json string.
	 *
	 * @return a string representation of this json element.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Language("json")
	@Contract(pure = true)
	String json();

	/**
	 * A string representation of this json element.
	 * <br>
	 * Always returns a valid json string.
	 *
	 * @param indent the base indent.
	 * @param tab    a single tab.
	 * @return a string representation of this json element.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Language("json")
	@Contract(pure = true)
	String json(@NotNull String indent, @NotNull String tab);
}
