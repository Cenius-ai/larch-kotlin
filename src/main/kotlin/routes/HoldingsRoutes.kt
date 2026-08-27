package ai.cenius.larch.routes

import ai.cenius.larch.plugins.ThymeleafPlugin
import ai.cenius.larch.repository.PortfolioRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.holdingsRoutes() {
    get("/holdings") {
        val holdings = PortfolioRepository.getAllHoldingsWithDetails()
        val totalValue = PortfolioRepository.getTotalValue()

        ThymeleafPlugin.render(call, "holdings", mapOf<String, Any>(
            "page" to "holdings",
            "holdings" to holdings,
            "totalValue" to totalValue
        ))
    }
}
