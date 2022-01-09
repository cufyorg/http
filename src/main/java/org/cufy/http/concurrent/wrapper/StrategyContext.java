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
package org.cufy.http.concurrent.wrapper;

import org.cufy.http.concurrent.Strategy;
import org.cufy.http.concurrent.Task;
import org.jetbrains.annotations.NotNull;

/**
 * A multipurpose performer wrapper.
 *
 * @param <Self> the type of the wrapper.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public interface StrategyContext<Self extends StrategyContext<Self>> extends StrategyWrapper<Self> {
	/**
	 * Perform the given {@code performance} with the current strategy and this as the
	 * parameter.
	 *
	 * @param task a function that accepts this and a callback.
	 * @return this.
	 * @throws NullPointerException if the given {@code performance} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	default Self perform(@NotNull Task<? super Self> task) {
		return this.perform((Self) this, task);
	}

	/**
	 * Perform the given {@code performance} with the current strategy and the given
	 * {@code parameter}.
	 *
	 * @param <T>       the type of the parameter.
	 * @param parameter the parameter to be passed to the task.
	 * @param task      a function that accepts the parameter and a callback.
	 * @return this.
	 * @throws NullPointerException if the given {@code task} is null.
	 * @since 0.3.0 ~2022.01.05
	 */
	default <T> Self perform(T parameter, @NotNull Task<T> task) {
		Strategy strategy = this.strategy();

		if (strategy == null)
			task.start(parameter, () -> {
			});
		else
			strategy.execute(callback -> {
				task.start(parameter, callback);
			});

		return (Self) this;
	}
}
