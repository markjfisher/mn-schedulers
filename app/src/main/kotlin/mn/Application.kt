package mn

import io.micronaut.runtime.Micronaut

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Micronaut.build()
                    .packages("mn")
                    .mainClass(Application::class.java)
                    .start()
        }
    }
}
