package org.cufy.http.misc

import org.cufy.http.connect.Callback
import org.cufy.http.connect.Client
import org.cufy.http.connect.Client.client
import org.cufy.http.kotlin.invoke
import org.junit.Test

class KMiscTest {
    @Test
    fun main() {
        val callback = Callback<String> { _: Client, param -> println(param) }

        callback(client(), "Hi")
    }
}
