/*
 *	Copyright 2021-2022 Cufy
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
package org.cufy.http.concurrent.wrapper

import org.cufy.http.concurrent.Strategy
import org.cufy.http.concurrent.SuspendStrategy
import org.cufy.http.concurrent.Task

// StrategyWrapper

/** An alias for [StrategyWrapper.strategy] */
var <Self : StrategyWrapper<*>> Self.strategy: Strategy?
    get() = strategy()
    set(v) = run { strategy(v) }

// TaskContext

/** A suspend version of [StrategyContext.perform]. */
suspend fun <Self : StrategyContext<*>> Self.performSuspend(task: Task<in Self>): Self =
    apply { this.performSuspend(this, task) }


/** A suspend version of [StrategyContext.perform]. */
suspend fun <T, Self : StrategyContext<*>> Self.performSuspend(
    parameter: T, task: Task<in T>
): Self {
    when (val strategy = this.strategy()) {
        null -> task.start(parameter) {
        }
        is SuspendStrategy -> strategy.executeSuspend {
            task.start(parameter, it)
        }
        else -> strategy.execute {
            task.start(parameter, it)
        }
    }

    return this
}
