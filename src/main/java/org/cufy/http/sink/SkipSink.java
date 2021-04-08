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

import java.util.Objects;

/**
 * A sink that skips runnables pushed to it when it is bussy.
 *
 * @author LSafer
 * @version 0.0.7
 * @since 0.0.7 ~2021.04.09
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class SkipSink implements Sink {
	/**
	 * The lock this sink is synchronized with.
	 *
	 * @since 0.0.1 ~2021.04.09
	 */
	protected final Object lock = new Object();
	/**
	 * True, indicates that this sink is currently bussy.
	 *
	 * @since 0.0.1 ~2021.04.09
	 */
	protected volatile boolean bussy;

	@NotNull
	@Override
	public Sink flush() {
		synchronized (this.lock) {
			this.bussy = false;
		}

		return this;
	}

	@NotNull
	@Override
	public Sink push(@NotNull Runnable runnable) {
		Objects.requireNonNull(runnable, "runnable");
		synchronized (this.lock) {
			if (!this.bussy) {
				this.bussy = true;
				runnable.run();
			}
		}

		return this;
	}
}
