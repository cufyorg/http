package org.cufy.http.groovy

import org.cufy.http.body.JsonBody
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.TextBody
import org.cufy.http.connect.Action
import org.cufy.http.connect.Callback
import org.cufy.http.connect.Client
import org.cufy.http.middleware.Middleware
import org.cufy.http.request.Headers
import org.cufy.http.uri.Query
import org.cufy.http.uri.UserInfo
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/**
 * Return a new action that have the names of the receiver action and the given {@code action}
 * and accepts both what the receiver action and the given {@code action} accepts.
 *
 * @param self the receiver action to be combined.
 * @param action the action to combine with the receiver action into a new action.
 * @return a new action from combining the receiver action and the given {@code action}.
 * @throws NullPointerException if the receiver or [action] is null.
 * @author LSafer
 * @since 0.2.8 ~2021.08.28
 */
@NotNull
static <T> Action<T> or(@NotNull Action<? extends T> self, @NotNull Action<? extends T> action) {
	Objects.requireNonNull(self, "self")
	Objects.requireNonNull(action, "action")
	return new Action<T>() {
		@Override
		boolean test(@NotNull String name, @Nullable Object parameter) {
			return self.test(name, parameter) ||
				   action.test(name, parameter)
		}

		@NotNull
		@Override
		Iterator<String> iterator() {
			def set = new HashSet(self.toList())
			set.addAll(action.toList())
			return set.iterator()
		}
	}
}

/**
 * Return a new callback that calls the receiver callback then the given {@code callback}
 * respectfully when called.
 *
 * @param self the receiver callback to be combined.
 * @param callback the callback to be called after the receiver callback when the returned
 *                 callback get called.
 * @return a new callback from combining the receiver callback and the given {@code callback}.
 * @throws NullPointerException if the receiver or {@code callback} is null.
 * @author LSafer
 * @since 0.2.8 ~2021.08.28
 */
@NotNull
static <T> Callback<T> rightShift(@NotNull Callback<? super T> self, @NotNull Callback<? super T> callback) {
	Objects.requireNonNull(self, "self")
	Objects.requireNonNull(callback, "callback")
	return { t, u ->
		self.call(t, u)
		callback.call(t, u)
	}
}

/**
 * Allows to use the index operator for storing values in a json body.
 */
@NotNull
static JsonBody putAt(@NotNull JsonBody self, @NotNull String name, @NotNull Object value) {
	return self.put(name, value)
}

/**
 * Allows to use the index operator for storing values in a parameters body.
 */
@NotNull
static ParametersBody putAt(@NotNull ParametersBody self, @NotNull String name, @NotNull String value) {
	return self.put(name, value)
}

/**
 * Allows to use the index operator for storing values in headers.
 */
@NotNull
static Headers putAt(@NotNull Headers self, @NotNull String name, @NotNull String value) {
	return self.put(name, value)
}

/**
 * Allows to use the index operator for storing values in a query.
 */
@NotNull
static Query putAt(@NotNull Query self, @NotNull String name, @NotNull String value) {
	return self.put(name, value)
}

/**
 * Allows to use the index operator for storing values in an userinfo.
 */
@NotNull
static UserInfo putAt(@NotNull UserInfo self, int index, @NotNull String value) {
	return self.put(index, value)
}

/**
 * Allows to use the appending operator for storing values in an userinfo.
 */
@NotNull
static TextBody leftShift(@NotNull TextBody self, @NotNull Object value) {
	self.append(value)
}

/**
 * A shortcut for {@link Client#use}.
 */
@NotNull
static Client leftShift(@NotNull Client self, @NotNull Middleware middleware) {
	return self.use(middleware)
}
