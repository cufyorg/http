package org.cufy.http.ext;

import org.cufy.http.Body;
import org.cufy.http.Headers;
import org.cufy.http.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A class representing a part in a {@link MultipartBody multipart body}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.18
 */
public class BodyPart extends Message {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -167302082529388006L;

	/**
	 * Construct a new part with the given parameters.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	public BodyPart() {
		this.headers = new Headers();
		this.body = null;
	}

	/**
	 * Construct a new part with the given parameters.
	 *
	 * @param headers the headers of the constructed part.
	 * @param body    the body of the constructed part.
	 * @throws NullPointerException if the given {@code headers} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public BodyPart(@NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		this.body = body;
	}

	/**
	 * Construct a new part with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new part.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public BodyPart(@NotNull Consumer<@NotNull BodyPart> builder) {
		Objects.requireNonNull(builder, "builder");
		this.headers = new Headers();
		this.body = null;
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Capture this part into a new object.
	 *
	 * @return a clone of this part.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Override
	@Contract(value = "->new", pure = true)
	public BodyPart clone() {
		return (BodyPart) super.clone();
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof BodyPart) {
			BodyPart part = (BodyPart) object;

			return Objects.equals(this.headers, part.headers) &&
				   Objects.equals(this.body, part.body);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

	@Override
	public String toString() {
		return this.headers + "\r\n\r\n" + this.body;
	}
}
