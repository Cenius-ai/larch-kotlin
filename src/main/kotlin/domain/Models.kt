package ai.cenius.larch.domain

import java.math.BigDecimal

data class Asset(
    val id: Long = 0,
    val symbol: String,
    val name: String,
    val currentPriceUsd: BigDecimal,
    val category: String? = null
)

data class Holding(
    val id: Long = 0,
    val assetId: Long,
    val quantity: BigDecimal,
    val purchasePriceUsd: BigDecimal? = null,
    val purchaseDate: String? = null
)

data class HoldingDetail(
    val id: Long,
    val assetId: Long,
    val symbol: String,
    val name: String,
    val quantity: BigDecimal,
    val currentPriceUsd: BigDecimal,
    val valueUsd: BigDecimal,
    val purchasePriceUsd: BigDecimal?,
    val purchaseDate: String?,
    val category: String?
)
