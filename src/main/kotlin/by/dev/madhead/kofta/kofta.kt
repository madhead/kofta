@file:JvmName("Kofta")

package by.dev.madhead.kofta

import java.io.File
import java.util.Random
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

const val PRIORITY_BASE = 0xDEAD

fun main(args: Array<String>) {
    val configuration = Configuration.read(File(System.getenv("NETDATA_CONFIG_DIR")!!, "kofta.yml"))

    configuration.servers.forEachIndexed { index, server ->
        println("CHART 'Kafka.consumers_by_topic_${server.name}' '${server.name}' 'Consumers by topic for ${server.name}' 'consumers' '${server.name}' 'consumers_by_topic' line ${PRIORITY_BASE + index} ${server.refreshInterval}")
        server.topics.forEach {
            println("DIMENSION 'consumers_by_topic_$it' '$it' absolute")
        }
    }

    val executor = Executors.newScheduledThreadPool(1) { runnable ->
        Thread(runnable, "kofta")
    }

    configuration.servers.forEachIndexed { index, server ->
        executor.scheduleAtFixedRate({
            synchronized(System.out) {
                println("BEGIN 'Kafka.consumers_by_topic_${server.name}'")
                server.topics.forEach {
                    println("""
                        SET 'consumers_by_topic_$it' = ${Random().nextInt(1000)}
                    """.trimIndent())
                }
                println("END")
            }
        }, 0, server.refreshInterval, TimeUnit.SECONDS)
    }
}
