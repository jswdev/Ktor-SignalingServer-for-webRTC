package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = 9527, host = "ezwrite-web.lvh.me") {
        configureRouting()
        configureSockets()
        configureSerialization()
        configureTemplating()
        configureSecurity()
    }.start(wait = true)
}
