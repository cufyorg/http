package org.cufy.http.sink;

import org.cufy.http.connect.Client;
import org.cufy.http.middleware.OkHttpMiddleware;
import org.junit.Assert;
import org.junit.Test;

public class SinkTest {
	@Test
	public void flushSink() throws InterruptedException {
		Sink sink = new FlushSink();
		int[] i = {0};
		Client.to("http://example.com")
			  .middleware(OkHttpMiddleware.middleware())
			  .on(Client.CONNECT, (c, r) -> {
				  i[0] *= 7;
			  })
			  .on(Client.CONNECTED, (c, r) -> {
				  i[0] += 3;
				  Thread.sleep(10);
				  sink.flush();
			  })
			  .on(Client.DISCONNECTED, (c, r) -> {
				  i[0] += 3;
				  Thread.sleep(10);
				  sink.flush();
			  })
			  .connect(sink)
			  .connect(sink);

		Thread.sleep(1_000);
		/*YES	( 0 * 7 + 3 ) * 7 + 3 = 24 */
		/*NO	0 * 7 * 7 + 3 + 3 = 6 */
		Assert.assertSame("Connection Clash", 24, i[0]);
	}

	@Test
	public void skipSink() throws InterruptedException {
		Sink sink = new SkipSink();
		int[] i = {0};
		Client.to("http://localhost/v2/items")
			  .middleware(OkHttpMiddleware.middleware())
			  .on(Client.CONNECT, (c, r) -> {
				  i[0] *= 7;
			  })
			  .on(Client.CONNECTED, (c, r) -> {
				  i[0] += 3;
				  Thread.sleep(100);
				  sink.flush();
			  })
			  .on(Client.DISCONNECTED, (c, r) -> {
				  i[0] += 3;
				  Thread.sleep(100);
				  sink.flush();
			  })
			  .connect(sink)
			  .connect(sink);

		Thread.sleep(5_000);
		/*YES	0 * 7 + 3 = 3 */
		/*NO	0 * 7 * 7 + 3 + 3 = 6 */
		Assert.assertSame("Connection Clash", 3, i[0]);
	}
}
