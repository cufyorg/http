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
public interface Performer<Self extends Performer<Self>> extends StrategyWrapper<Self> {
	/**
	 * Perform the given {@code performance} with the current performer.
	 *
	 * @param task a function that accepts this and a callback.
	 * @return this.
	 * @throws NullPointerException if the given {@code performance} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	default Self perform(@NotNull Task<? super Self> task) {
		Strategy strategy = this.strategy();

		if (strategy == null)
			task.start((Self) this, () -> {
			});
		else
			strategy.execute(callback -> {
				task.start((Self) this, callback);
			});

		return (Self) this;
	}
}