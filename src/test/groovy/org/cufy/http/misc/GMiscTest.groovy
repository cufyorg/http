package org.cufy.http.misc

import org.cufy.http.body.text.TextBody
import org.cufy.http.connect.Callback
import org.cufy.http.request.Request
import org.cufy.http.response.Response
import org.cufy.http.response.StatusLine
import org.junit.Test

import static org.cufy.http.connect.Client.client

class GMiscTest {
	@Test
	void build() {
		println Request.request("GET / HTTP/1.1\n")
					   .setBody("Hi I'm glad to see you!")

		def r = Request.request()
					   .requestLine {
						   it.setHttpVersion "HTTP/1.1"
						   it.uri {
							   it.authority.userInfo {
								   it.put 0, "admin"
								   it.put 1, "admin"
							   }
							   it.query {
								   it.put "name", "age"
							   }
						   }
					   }
					   .setBody(TextBody.text())
					   .body({
						   //it.append("a", "b", "c")
					   })

		println r
	}

	@Test
	void parse() {
		def r = StatusLine.statusLine("HTTP/1.1 200 OK")

		println r.getHttpVersion()
		println r.getStatusCode()
		println r.getReasonPhrase()
	}

	@Test
	void parse2() {
		def r = Response.response("""\
HTTP/1.1 200 OK
date: Mon, 22 Mar 2021 15:42:54 GMT
cache-control: public, s-maxage=31536000, max-age=31536000, immutable
server: ATS/8.0.8
x-content-type-options: nosniff
access-control-allow-origin: *
last-modified: Tue, 16 Mar 2021 18:28:07 GMT
content-type: image/svg+xml
content-encoding: gzip
vary: Accept-Encoding
age: 38235
x-cache: cp3064 hit, cp3064 hit/1339308
x-cache-status: hit-front
server-timing: cache;desc="hit-front"
strict-transport-security: max-age=106384710; includeSubDomains; preload
report-to: { "group": "wm_nel", "max_age": 86400, "endpoints": [{ "url": "https://intake-logging.wikimedia.org/v1/events?stream=w3c.reportingapi.network_error&schema_uri=/w3c/reportingapi/network_error/1.0.0" }] }
nel: { "report_to": "wm_nel", "max_age": 86400, "failure_fraction": 0.05, "success_fraction": 0.0}
accept-ranges: bytes
content-length: 281
X-Firefox-Spdy: h2

        """)
		def x = r.clone()

		print(r == x)
	}

	@Test
	void calling() {
		Callback<String> callback = { client, param -> println param }

		callback(client(), "HI")
	}
}
