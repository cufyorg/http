package org.cufy.http.misc

import org.cufy.http.model.Callback
import org.cufy.http.impl.ClientImpl
import org.cufy.http.model.Client
import org.cufy.http.ext.invoke
import org.junit.Test

class KMiscTest {
    @Test
    fun main() {
        val callback =
            Callback<String> { _: Client, param ->
                println(param)
            }

        callback(ClientImpl(), "Hi")
    }
}
