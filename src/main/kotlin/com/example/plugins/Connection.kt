package com.example.plugins

import io.ktor.http.cio.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession) {
    val name = "user${lastId.getAndIncrement()}"
    companion object {
        var lastId = AtomicInteger(0)
    }
}