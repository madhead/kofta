package by.dev.madhead.kofta

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ConfigurationTest {
    @Test
    fun read() {
        val configuration = Configuration.read(File(ConfigurationTest::class.java.getResource("kofta.yml").toURI()))

        Assertions.assertEquals(
            Configuration(
                servers = listOf(
                    Server(
                        name = "Millennium Falcon",
                        host = "millennium-falcon",
                        port = 9092,
                        refreshInterval = 5,
                        consumerGroups = listOf(
                            "consumer 1",
                            "consumer 2",
                            "consumer 3"
                        ),
                        topics = listOf(
                            "topic 1"
                        )
                    ),
                    Server(
                        name = "Ebon Hawk",
                        host = "ebon-hawk",
                        port = 9092,
                        refreshInterval = 10,
                        consumerGroups = listOf(
                            "consumer 1"
                        ),
                        topics = listOf(
                            "topic 1",
                            "topic 2",
                            "topic 3",
                            "topic 4"
                        )
                    )
                )
            ),
            configuration
        )
    }
}
