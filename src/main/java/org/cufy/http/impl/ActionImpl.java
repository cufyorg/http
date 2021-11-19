package org.cufy.http.impl;

import org.cufy.http.model.Action;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A basic implementation of the interface {@link Action}.
 *
 * @param <T> the type of parameter this action accepts.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.18
 */
public class ActionImpl<T> implements Action<T> {
	/**
	 * The names this action triggers.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	protected final Set<@NotNull String> names;
	/**
	 * The predicate this action is using.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	protected final BiPredicate<@NotNull String, @Nullable Object> predicate;

	/**
	 * Construct a new action from the given component.
	 *
	 * @param names     the names the constructed action will trigger.
	 * @param predicate the predicate the constructed action will use.
	 * @throws NullPointerException if the given {@code names} or {@code predicate} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@ApiStatus.Internal
	public ActionImpl(@NotNull Set<@NotNull String> names, @NotNull BiPredicate<@NotNull String, @NotNull Object> predicate) {
		Objects.requireNonNull(names, "names");
		Objects.requireNonNull(predicate, "predicate");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.names = names;
		this.predicate = predicate;
	}

	@NotNull
	@Override
	public Iterator<@NotNull String> iterator() {
		return this.names.iterator();
	}

	@Override
	public boolean test(@NotNull String name, @Nullable Object parameter) {
		return this.predicate.test(name, parameter);
	}
}
