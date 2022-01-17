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
package org.cufy.http.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A performer is a function that accepts a function and gives a callback.
 * <br>
 * The performer is expected to run the function given to it and waits (sleeps) until its
 * callback gets invoked.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public abstract class Performer {
	/**
	 * A performer implementation that uses the {@link Object#wait() wait method} and
	 * locks to operate.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Performer WAIT = new Performer() {
		@Override
		public void execute(@NotNull Runnable block, @NotNull Consumer<@NotNull Runnable> callbackConsumer) {
			boolean[] mutex = {false};

			synchronized (mutex) {
				callbackConsumer.accept(() -> {
					synchronized (mutex) {
						mutex[0] = true;
						mutex.notifyAll();
					}
				});

				block.run();

				while (!mutex[0])
					try {
						mutex.wait();
					} catch (InterruptedException ignored) {
					}
			}
		}
	};

	/**
	 * Invoke the given {@code block} with a callback to be invoked when the operation is
	 * over.
	 *
	 * @param block the block to be invoked.
	 * @throws NullPointerException if the given {@code block} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public void execute(@NotNull Consumer<@NotNull Runnable> block) {
		Objects.requireNonNull(block, "block");
		AtomicReference<Runnable> atomicCallback = new AtomicReference<>();
		AtomicBoolean atomicState = new AtomicBoolean();

		this.execute(
				() -> {
					Runnable callback = atomicCallback.get();

					if (callback == null)
						throw new IllegalStateException("Callback not provided");

					if (atomicState.get())
						throw new IllegalStateException("Block already executed");

					atomicState.set(true);
					block.accept(callback);
				},
				callback -> {
					if (atomicCallback.get() != null)
						throw new IllegalStateException("Callback already provided");

					atomicCallback.set(callback);
				}
		);
	}

	/**
	 * Invoke the given {@code callbackConsumer} with a callback to be invoked when the
	 * operation is over. Then, invoke the given {@code block} and wait until the callback
	 * is invoked.
	 * <br>
	 * This function must also make sure to not wait if the callback got invoked while
	 * invoking the block.
	 * <br>
	 * The callback given by this performer can be called twice.
	 *
	 * @param block            the function to be invoked; this function must throw an
	 *                         {@link IllegalStateException} if it got invoked before
	 *                         {@code callbackConsumer} or called twice.
	 * @param callbackConsumer a function to call to give the callback to the caller.
	 * @throws NullPointerException if the given {@code block} or {@code callbackConsumer}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public abstract void execute(@NotNull Runnable block, @NotNull Consumer<@NotNull Runnable> callbackConsumer);
}
