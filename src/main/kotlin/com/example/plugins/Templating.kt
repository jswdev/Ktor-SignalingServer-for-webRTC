package com.example.plugins

import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.configureTemplating() {

    routing {
        get("/echo") {
            call.respondHtml {
                body {
                    h1 { +"Signaling Server" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }
    }
}
suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
   this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

