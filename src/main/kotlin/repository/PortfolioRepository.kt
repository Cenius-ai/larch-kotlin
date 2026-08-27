package ai.cenius.larch.repository

import ai.cenius.larch.db.Assets
import ai.cenius.larch.db.Holdings
import ai.cenius.larch.domain.Holding
import ai.cenius.larch.domain.HoldingDetail
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.math.RoundingMode

object PortfolioRepository {

    private val holdingJoin = Holdings.innerJoin(Assets) { Holdings.assetId eq Assets.id }

    fun getAllHoldings(): List<Holding> = transaction {
        Holdings.selectAll()
            .orderBy(Holdings.id)
            .map { row ->
                Holding(
                    id = row[Holdings.id],
                    assetId = row[Holdings.assetId],
                    quantity = row[Holdings.quantity],
                    purchasePriceUsd = row[Holdings.purchasePriceUsd],
                    purchaseDate = row[Holdings.purchaseDate]?.toString()
                )
            }
    }

    fun getAllHoldingsWithDetails(): List<HoldingDetail> = transaction {
        holdingJoin
            .selectAll()
            .orderBy(Assets.symbol)
            .map { row ->
                val qty = row[Holdings.quantity]
                val price = row[Assets.currentPriceUsd]
                HoldingDetail(
                    id = row[Holdings.id],
                    assetId = row[Holdings.assetId],
                    symbol = row[Assets.symbol],
                    name = row[Assets.name],
                    quantity = qty,
                    currentPriceUsd = price,
                    valueUsd = qty.multiply(price).setScale(2, RoundingMode.HALF_UP),
                    purchasePriceUsd = row[Holdings.purchasePriceUsd],
                    purchaseDate = row[Holdings.purchaseDate]?.toString(),
                    category = row[Assets.category]
                )
            }
    }

    fun getAllAssets(): List<Pair<Long, String>> = transaction {
        Assets.selectAll()
            .orderBy(Assets.symbol)
            .map { it[Assets.id] to "${it[Assets.symbol]} — ${it[Assets.name]}" }
    }

    fun addHolding(aid: Long, qty: BigDecimal): Holding = transaction {
        Holdings.insert { stmt ->
            stmt[assetId] = aid
            stmt[quantity] = qty
        }
        val maxId = Holdings.selectAll().maxOf { it[Holdings.id] }
        Holding(id = maxId, assetId = aid, quantity = qty)
    }

    fun deleteHolding(id: Long): Boolean = transaction {
        Holdings.deleteWhere { Holdings.id eq id } > 0
    }

    fun getTotalValue(): BigDecimal = transaction {
        holdingJoin
            .selectAll()
            .map { row ->
                row[Holdings.quantity].multiply(row[Assets.currentPriceUsd])
            }
            .fold(BigDecimal.ZERO) { acc, v -> acc.add(v) }
            .setScale(2, RoundingMode.HALF_UP)
    }

    fun getAllocationData(): List<AllocationEntry> = transaction {
        val total = getTotalValue()
        if (total.compareTo(BigDecimal.ZERO) == 0) return@transaction emptyList()

        holdingJoin
            .selectAll()
            .map { row ->
                val value = row[Holdings.quantity].multiply(row[Assets.currentPriceUsd])
                val pct = value.multiply(BigDecimal("100")).divide(total, 4, RoundingMode.HALF_UP)
                AllocationEntry(
                    symbol = row[Assets.symbol],
                    name = row[Assets.name],
                    valueUsd = value.setScale(2, RoundingMode.HALF_UP),
                    percentage = pct.setScale(2, RoundingMode.HALF_UP)
                )
            }
            .sortedByDescending { it.valueUsd }
    }
}

data class AllocationEntry(
    val symbol: String,
    val name: String,
    val valueUsd: BigDecimal,
    val percentage: BigDecimal
)
