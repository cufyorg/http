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
package org.cufy.http.body;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * An unmodifiable empty implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class EmptyBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9114967605139468231L;

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@NotNull
	@Override
	public EmptyBody clone() {
		return this;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		return object instanceof EmptyBody;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Range(from = 0, to = Long.MAX_VALUE)
	@Override
	public long length() {
		return 0;
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return "";
	}
}
