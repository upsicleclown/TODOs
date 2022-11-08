package todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import todo.database.config.SQLiteDBConfig

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SQLiteDBConfig().initialize()
    runApplication<Application>(*args)
}
