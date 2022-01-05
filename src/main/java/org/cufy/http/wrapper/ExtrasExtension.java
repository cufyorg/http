/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.wrapper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A cursor with shortcut extras accessors.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.05
 */
public interface ExtrasExtension<Self extends ExtrasExtension<Self>> extends ExtrasWrapper<Self> {
	// Extras

	/**
	 * Get the extra with the given {@code name}.
	 *
	 * @param name the name of the extra.
	 * @return the extra with the given {@code name}.
	 * @throws ClassCastException   if the name is of an inappropriate type for the extras
	 *                              map.
	 * @throws NullPointerException if the name is null and the extras map does not permit
	 *                              null keys.
	 * @since 1.0.0 ~2022.01.05
	 */
	@Nullable
	@Contract(pure = true)
	default Object extra(@Nullable String name) {
		return this.extras().get(name);
	}

	/**
	 * Put an extra with the given {@code name} and the given {@code value}.
	 *
	 * @param name  the name of the extra to be put.
	 * @param value the value of the extra to be put.
	 * @return this.
	 * @throws UnsupportedOperationException if the put operation is not supported by the
	 *                                       extras map.
	 * @throws ClassCastException            if the class of the specified name or value
	 *                                       prevents it from being stored in the extras
	 *                                       map
	 * @throws NullPointerException          if the specified name or value is null and
	 *                                       the extras map does not permit null keys or
	 *                                       values
	 * @throws IllegalArgumentException      if some property of the specified name or
	 *                                       value prevents it from being stored in the
	 *                                       extras map
	 * @since 1.0.0 ~2022.01.05
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Self extra(@Nullable String name, @Nullable Object value) {
		this.extras().put(name, value);
		return (Self) this;
	}
}
