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
package org.cufy.http.concurrent.wrapper

import org.cufy.http.concurrent.SuspendStrategy
import org.cufy.http.concurrent.Task

/**
 * A suspend version of [TaskContext.perform].
 */
suspend fun <T : TaskContext<T>> T.performSuspend(task: Task<in T>): T {
    when (val performer = this.strategy()) {
        null -> task.start(this) {
        }
        is SuspendStrategy -> performer.performSuspend {
            task.start(this, it)
        }
        else -> performer.execute {
            task.start(this, it)
        }
    }

    return this
}
