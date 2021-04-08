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
package org.cufy.http.sink;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A sink is a list of runnables that have been {@link #push(Runnable) pushed to it} and
 * might get executed sometimes in the feature. A sink has an optional business state and
 * if the sink is bussy it might skip some runnables or make them wait until it is no more
 * bussy.
 *
 * @author LSafer
 * @version 0.0.7
 * @since 0.0.7 ~2021.04.09
 */
public interface Sink {
	/**
	 * Flush this sink. In other words, tell this sink that the previously executed
	 * runnable was done its work and this sink is no longer bussy.
	 *
	 * @return this.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract(value = "->this", mutates = "this")
	Sink flush();

	/**
	 * Push the given {@code runnable} to be invoked sometime in the feature. Some sink
	 * implementation might wait until all the previously pushed runnables are executed
	 * other implementations might just ignore the given {@code runnable} or just run it
	 * rightaway. It highly depend on the implementation.
	 *
	 * @param runnable the runnable to be executed.
	 * @return this.
	 * @throws NullPointerException if the given {@code runnable} is null.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Sink push(@NotNull Runnable runnable);
}
