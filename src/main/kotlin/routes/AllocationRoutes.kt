package ai.cenius.larch.routes

import ai.cenius.larch.repository.PortfolioRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class AllocationJson(
    val symbol: String,
    val name: String,
    val valueUsd: String,
    val percentage: String
)

fun Route.allocationRoutes() {
    get("/api/allocation") {
        val data = PortfolioRepository.getAllocationData()
        val json = data.map {
            AllocationJson(
                symbol = it.symbol,
                name = it.name,
                valueUsd = it.valueUsd.toPlainString(),
                percentage = it.percentage.toPlainString()
            )
        }
        call.respondText(Json.encodeToString(json), ContentType.Application.Json)
    }
}
