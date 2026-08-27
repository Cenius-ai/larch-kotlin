package ai.cenius.larch.routes

import ai.cenius.larch.plugins.ThymeleafPlugin
import ai.cenius.larch.repository.PortfolioRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigDecimal

fun Route.addHoldingRoutes() {
    get("/holdings/add") {
        val assets = PortfolioRepository.getAllAssets()
        ThymeleafPlugin.render(call, "add-holding", mapOf<String, Any>(
            "page" to "add",
            "assets" to assets,
            "error" to ""
        ))
    }

    post("/holdings/add") {
        val formParams = call.receiveParameters()
        val assetIdStr = formParams["assetId"] ?: ""
        val quantityStr = formParams["quantity"] ?: ""

        val errors = mutableListOf<String>()

        val assetId = assetIdStr.toLongOrNull()
        if (assetId == null || assetId <= 0) {
            errors.add("Please select a valid asset.")
        }

        val quantity = quantityStr.toBigDecimalOrNull()
        if (quantity == null || quantity <= BigDecimal.ZERO) {
            errors.add("Please enter a valid quantity greater than zero.")
        }

        if (errors.isNotEmpty()) {
            val assets = PortfolioRepository.getAllAssets()
            return@post ThymeleafPlugin.render(call, "add-holding", mapOf<String, Any>(
                "page" to "add",
                "assets" to assets,
                "error" to errors.joinToString(" "),
                "selectedAssetId" to assetIdStr,
                "quantityValue" to quantityStr
            ))
        }

        PortfolioRepository.addHolding(assetId!!, quantity!!)
        call.respondRedirect("/holdings")
    }
}
