package com.example.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/echo") { // websocketSession
            val thisConnection = Connection(this)
            connections += thisConnection
            send("You've logged in as [${thisConnection.name}]")
            println("You've logged in as [${thisConnection.name}]")
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readBytes()
                        println("onMessage")
                        connections.forEach {
                            it.session.send(text)
                        }
                    }
                }
            }
        }
    }
}
