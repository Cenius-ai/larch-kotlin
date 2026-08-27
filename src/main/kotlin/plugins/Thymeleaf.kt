package ai.cenius.larch.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

object ThymeleafPlugin {

    private val engine: TemplateEngine by lazy {
        val resolver = ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            isCacheable = false
        }
        TemplateEngine().apply {
            setTemplateResolver(resolver)
        }
    }

    fun configure() {
        // engine is lazily initialized on first use
    }

    suspend fun render(call: ApplicationCall, template: String, model: Map<String, Any> = emptyMap()) {
        val context = Context().apply {
            model.forEach { (key, value) -> setVariable(key, value) }
        }
        val html = engine.process(template, context)
        call.respondText(html, io.ktor.http.ContentType.Text.Html)
    }
}
