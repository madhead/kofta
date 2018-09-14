package by.dev.madhead.kofta

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

data class Configuration(
    val servers: List<Server>
) {
    companion object {
        fun read(file: File): Configuration = ObjectMapper(YAMLFactory()).registerKotlinModule().readValue(file)
    }
}

data class Server(
    val name: String,
    val host: String,
    val port: Int,
    val refreshInterval: Long,
    val consumerGroups: List<String>,
    val topics: List<String>
)
