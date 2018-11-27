package mn

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.lang.Exception

private val logger = KotlinLogging.logger {}

@Controller("/api")
class ProductCatalogueController {

    @Get("/{id}")
    fun getId(id: String): Maybe<String> {
        logger.info { "getId: on thread ${Thread.currentThread().name}"}
        return Maybe
                .just(id)
                .subscribeOn(Schedulers.io())
                .doOnEvent { success, throwable ->
                    try { Thread.sleep(500) } catch (_: Exception) {}
                    logger.info { "doOnEvent called success: $success, throwable: $throwable, thread: ${Thread.currentThread().name}" }
                }
                .map { it }
    }
}
