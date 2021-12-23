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
package org.cufy.http.concurrent.cursor

import org.cufy.http.concurrent.Performance
import org.cufy.http.concurrent.SuspendPerformer

/**
 * A suspend version of [Perform.perform].
 */
suspend fun <T : Perform<T>> T.performSuspend(performance: Performance<in T>): T {
    when (val performer = this.performer()) {
        null -> performance.perform(this) {
        }
        is SuspendPerformer -> performer.performSuspend {
            performance.perform(this, it)
        }
        else -> performer.perform {
            performance.perform(this, it)
        }
    }

    return this
}
