/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.client;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An emission is a set of names an operation is triggering.
 *
 * @param <T> the type of parameter that will be passed for this emission.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.13
 */
public class Emission<T> implements Iterable<String> {
	/**
	 * An emission that emits an exception when something wrong happens.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	public static final Emission<Throwable> EXCEPTION = new Emission<>("exception");

	/**
	 * The names this emission is triggering.
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	protected final Set<String> names;

	/**
	 * Construct a new emission instance that triggers the given {@code names}.
	 *
	 * @param names the names the constructed emission will be for.
	 * @throws NullPointerException if the given {@code names} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	public Emission(@Nullable String @NotNull ... names) {
		Objects.requireNonNull(names, "names");
		this.names = Arrays.stream(names)
						   .filter(Objects::nonNull)
						   .collect(Collectors.toSet());
	}

	/**
	 * Construct a new emission instance that triggers the given {@code names}.
	 *
	 * @param names the names the constructed emission will be for.
	 * @throws NullPointerException if the given {@code names} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	public Emission(@NotNull Collection<String> names) {
		Objects.requireNonNull(names, "names");
		this.names = names
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/**
	 * Construct a new emission from combining the given {@code emissions}.
	 *
	 * @param emissions the emissions to be combined.
	 * @param <E>       the common type between the given {@code emissions}.
	 * @return a new emission from combining the given {@code emissions}.
	 * @throws NullPointerException if the given {@code emissions} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	public static <E> Emission<E> combine(@Nullable Emission<? super E> @NotNull ... emissions) {
		Objects.requireNonNull(emissions, "emissions");
		return new Emission<>(
				Arrays.stream(emissions)
					  .flatMap(it -> it.names.stream())
					  .collect(Collectors.toSet())
		);
	}

	@NotNull
	@Override
	public Iterator<String> iterator() {
		return this.names.iterator();
	}
}
