package com.example.plugins

import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.locations.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.configureSecurity() {
    
    install(Authentication) {
            oauth("auth-oauth-google") {
                urlProvider = { "http://localhost:8080/callback" }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "google",
                        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                        accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                        requestMethod = HttpMethod.Post,
                        clientId = System.getenv("GOOGLE_CLIENT_ID"),
                        clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                        defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                    )
                }
                client = HttpClient(Apache)
            }
        }
    authentication {
    		basic(name = "myauth1") {
    			realm = "Ktor Server"
    			validate { credentials ->
    				if (credentials.name == credentials.password) {
    					UserIdPrincipal(credentials.name)
    				} else {
    					null
    				}
    			}
    		}
    
    	    form(name = "myauth2") {
    	        userParamName = "user"
    	        passwordParamName = "password"
    	        challenge {
    	        	/**/
    			}
    	    }
    	}

    routing {
        authenticate("auth-oauth-google") {
                    get("login") {
                        call.respondRedirect("/callback")
                    }
        
                    get("/callback") {
                        val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                        call.sessions.set(UserSession(principal?.accessToken.toString()))
                        call.respondRedirect("/hello")
                    }
                }
        authenticate("myauth1") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
        authenticate("myauth1") {
            get("/protected/route/form") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}
class UserSession(accessToken: String)
