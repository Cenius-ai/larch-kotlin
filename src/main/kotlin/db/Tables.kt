package ai.cenius.larch.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Assets : Table("assets") {
    val id = long("id").autoIncrement()
    val symbol = varchar("symbol", 12).uniqueIndex()
    val name = varchar("name", 100)
    val currentPriceUsd = decimal("current_price_usd", 18, 6)
    val category = varchar("category", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Holdings : Table("holdings") {
    val id = long("id").autoIncrement()
    val assetId = long("asset_id")
    val quantity = decimal("quantity", 18, 8)
    val purchasePriceUsd = decimal("purchase_price_usd", 18, 6).nullable()
    val purchaseDate = date("purchase_date").nullable()

    override val primaryKey = PrimaryKey(id)
}
