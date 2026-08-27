package ai.cenius.larch.routes

import ai.cenius.larch.repository.PortfolioRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteHoldingRoutes() {
    post("/api/holdings/{id}/delete") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respondText("{\"error\":\"Invalid holding ID\"}", ContentType.Application.Json, HttpStatusCode.BadRequest)
            return@post
        }

        val deleted = PortfolioRepository.deleteHolding(id)
        if (deleted) {
            call.respondText("{\"success\":true}", ContentType.Application.Json)
        } else {
            call.respondText("{\"error\":\"Holding not found\"}", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }
}
