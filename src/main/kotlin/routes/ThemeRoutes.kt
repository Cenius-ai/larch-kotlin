package ai.cenius.larch.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.themeRoutes() {
    post("/api/theme") {
        val currentTheme = call.request.cookies["theme"] ?: "light"
        val newTheme = if (currentTheme == "dark") "light" else "dark"
        call.response.cookies.append(
            Cookie(
                name = "theme",
                value = newTheme,
                path = "/",
                maxAge = 365 * 24 * 60 * 60,
                httpOnly = false
            )
        )
        call.respondText("{\"theme\":\"$newTheme\"}", ContentType.Application.Json)
    }
}
