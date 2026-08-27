package ai.cenius.larch.routes

import ai.cenius.larch.plugins.ThymeleafPlugin
import ai.cenius.larch.repository.PortfolioRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.dashboardRoutes() {
    get("/") {
        val holdings = PortfolioRepository.getAllHoldingsWithDetails()
        val totalValue = PortfolioRepository.getTotalValue()
        val allocation = PortfolioRepository.getAllocationData()

        ThymeleafPlugin.render(call, "dashboard", mapOf<String, Any>(
            "page" to "dashboard",
            "holdings" to holdings,
            "totalValue" to totalValue,
            "allocation" to allocation,
            "holdingCount" to holdings.size
        ))
    }
}
