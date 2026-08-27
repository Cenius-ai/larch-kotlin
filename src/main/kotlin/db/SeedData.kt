package ai.cenius.larch.db

import ai.cenius.larch.domain.Asset
import ai.cenius.larch.domain.Holding
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate

object SeedData {

    private val seedAssets = listOf(
        Asset(symbol = "BTC", name = "Bitcoin", currentPriceUsd = BigDecimal("67432.18"), category = "Layer 1"),
        Asset(symbol = "ETH", name = "Ethereum", currentPriceUsd = BigDecimal("3521.77"), category = "Layer 1"),
        Asset(symbol = "SOL", name = "Solana", currentPriceUsd = BigDecimal("187.43"), category = "Layer 1"),
        Asset(symbol = "AAVE", name = "Aave", currentPriceUsd = BigDecimal("162.91"), category = "DeFi"),
        Asset(symbol = "UNI", name = "Uniswap", currentPriceUsd = BigDecimal("9.84"), category = "DeFi"),
        Asset(symbol = "LINK", name = "Chainlink", currentPriceUsd = BigDecimal("14.76"), category = "Oracle"),
        Asset(symbol = "ARB", name = "Arbitrum", currentPriceUsd = BigDecimal("0.89"), category = "Layer 2"),
        Asset(symbol = "OP", name = "Optimism", currentPriceUsd = BigDecimal("2.34"), category = "Layer 2"),
        Asset(symbol = "MKR", name = "Maker", currentPriceUsd = BigDecimal("2812.55"), category = "DeFi"),
        Asset(symbol = "SNX", name = "Synthetix", currentPriceUsd = BigDecimal("3.21"), category = "DeFi"),
    )

    private val seedHoldings = listOf(
        Holding(assetId = 1, quantity = BigDecimal("1.524"), purchasePriceUsd = BigDecimal("42100.00"), purchaseDate = "2024-01-15"),
        Holding(assetId = 2, quantity = BigDecimal("12.8"), purchasePriceUsd = BigDecimal("2890.00"), purchaseDate = "2024-02-20"),
        Holding(assetId = 3, quantity = BigDecimal("245.5"), purchasePriceUsd = BigDecimal("142.30"), purchaseDate = "2024-03-10"),
        Holding(assetId = 4, quantity = BigDecimal("18.2"), purchasePriceUsd = BigDecimal("95.40"), purchaseDate = "2024-04-05"),
        Holding(assetId = 5, quantity = BigDecimal("520.0"), purchasePriceUsd = BigDecimal("7.20"), purchaseDate = "2024-05-12"),
        Holding(assetId = 6, quantity = BigDecimal("340.0"), purchasePriceUsd = BigDecimal("11.50"), purchaseDate = "2024-06-01"),
        Holding(assetId = 7, quantity = BigDecimal("12500.0"), purchasePriceUsd = BigDecimal("0.72"), purchaseDate = "2024-07-18"),
        Holding(assetId = 8, quantity = BigDecimal("3100.0"), purchasePriceUsd = BigDecimal("1.85"), purchaseDate = "2024-08-22"),
    )

    fun seedIfEmpty() {
        transaction {
            val assetCount = Assets.selectAll().count()
            if (assetCount == 0L) {
                seedAssets.forEach { asset ->
                    Assets.insert {
                        it[symbol] = asset.symbol
                        it[name] = asset.name
                        it[currentPriceUsd] = asset.currentPriceUsd
                        it[category] = asset.category
                    }
                }
            }

            val holdingCount = Holdings.selectAll().count()
            if (holdingCount == 0L) {
                seedHoldings.forEach { holding ->
                    Holdings.insert {
                        it[assetId] = holding.assetId
                        it[quantity] = holding.quantity
                        it[purchasePriceUsd] = holding.purchasePriceUsd
                        it[purchaseDate] = holding.purchaseDate?.let { LocalDate.parse(it) }
                    }
                }
            }
        }
    }
}
