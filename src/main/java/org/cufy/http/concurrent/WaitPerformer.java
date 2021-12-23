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
package org.cufy.http.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A performer implementation that uses the {@link Object#wait() wait method} and locks to
 * operate.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public class WaitPerformer implements Performer {
	/**
	 * A global instance of this class.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	public static final WaitPerformer INSTANCE = new WaitPerformer();

	@Override
	public void perform(@NotNull Runnable block, @NotNull Consumer<@NotNull Runnable> callbackConsumer) {
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
}
