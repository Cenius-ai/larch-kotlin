package ai.cenius.larch

import ai.cenius.larch.db.SeedData
import ai.cenius.larch.db.Assets
import ai.cenius.larch.db.Holdings
import ai.cenius.larch.plugins.ThymeleafPlugin
import ai.cenius.larch.routes.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val dbUrl = System.getenv("DATABASE_URL") ?: "jdbc:h2:mem:larch;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    Database.connect(dbUrl, driver = "org.h2.Driver")

    transaction {
        SchemaUtils.create(Assets, Holdings)
    }
    SeedData.seedIfEmpty()

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        configureApp()
    }.start(wait = true)
}

fun Application.configureApp() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText(
                "<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Something went wrong</h1><p>An unexpected error occurred. Please try again.</p></body></html>",
                ContentType.Text.Html,
                HttpStatusCode.InternalServerError
            )
        }
    }

    ThymeleafPlugin.configure()

    routing {
        staticResources("/static", "static")

        dashboardRoutes()
        holdingsRoutes()
        addHoldingRoutes()
        deleteHoldingRoutes()
        allocationRoutes()
        themeRoutes()
    }
}
