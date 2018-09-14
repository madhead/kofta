@file:JvmName("Kofta")

package by.dev.madhead.kofta

import java.util.Random
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val period = args[0].toLong();

    println("""
        CHART kafka.lag Name Title Unit Family Context line 57005
        DIMENSION messages "Messages dimension name" absolute 1 1
    """.trimIndent())

    while (true) {
        println("""
            BEGIN kafka.lag
            SET messages = ${Random().nextInt(100)}
            END
        """.trimIndent())
        Thread.sleep(TimeUnit.SECONDS.toMillis(period));
    }
}
