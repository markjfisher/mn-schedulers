package mn

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.util.concurrent.ExecutorService
import javax.inject.Named

private val logger = KotlinLogging.logger {}

@Controller("/api")
class Controller(@Named(TaskExecutors.IO) private val executionService: ExecutorService) {

    @Get("/{id}")
    fun getId(id: String): Maybe<String> {
        logger.info { "getId: on thread ${Thread.currentThread().name}"}
        return Maybe
                .just(id)
                .subscribeOn(Schedulers.from(executionService))
                .doOnEvent { success, throwable ->
                    try { Thread.sleep(500) } catch (_: Exception) {}
                    logger.info { "doOnEvent called success: $success, throwable: $throwable, thread: ${Thread.currentThread().name}" }
                }
                .map { it }
    }
}
