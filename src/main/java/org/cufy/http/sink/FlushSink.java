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

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 * A sink that saves the runnables that was pushed to it when it was bussy to invoke them
 * when the sink get flushed.
 *
 * @author LSafer
 * @version 0.0.7
 * @since 0.0.7 ~2021.04.09
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class FlushSink implements Sink {
	/**
	 * The lock this sink is synchronized with.
	 *
	 * @since 0.0.1 ~2021.04.09
	 */
	protected final Object lock = new Object();
	/**
	 * The runnables waiting to be executed when this sink is not bussy.
	 *
	 * @since 0.0.1 ~2021.04.09
	 */
	protected final Deque<Runnable> runnables = new LinkedList<>();

	@NotNull
	@Override
	public Sink flush() {
		synchronized (this.lock) {
			//remove the last executed runnable
			this.runnables.removeFirst();

			//get the next runnable to be executed
			Runnable runnable = this.runnables.pollFirst();

			if (runnable != null)
				runnable.run();
		}

		return this;
	}

	@NotNull
	@Override
	public Sink push(@NotNull Runnable runnable) {
		Objects.requireNonNull(runnable, "runnable");
		synchronized (this.lock) {
			this.runnables.addLast(runnable);

			if (this.runnables.size() == 1)
				//not bussy
				runnable.run();
		}

		return this;
	}
}
