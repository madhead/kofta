@file:JvmName("Kofta")

package by.dev.madhead.kofta

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

const val PRIORITY_BASE = 0xDEAD

fun main(args: Array<String>) {
    val configuration = Configuration.read(File(System.getenv("NETDATA_CONFIG_DIR")!!, "kofta.yml"))
    val executor = Executors.newScheduledThreadPool(1) { runnable ->
        Thread(runnable, "kofta")
    }

    configuration.servers.forEachIndexed { index, server ->
        println("CHART 'Kafka.consumers_by_topic_${server.name}' '${server.name}' 'Consumers by topic for ${server.name}' 'consumers' '${server.name}' 'consumers_by_topic' line ${PRIORITY_BASE + index} ${server.refreshInterval}")
        server.topics.forEach {
            println("DIMENSION 'consumers_by_topic_$it' '$it' absolute")
        }

        val client = AdminClient.create(mapOf(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to "${server.host}:${server.port}"
        ))

        executor.scheduleAtFixedRate({
            client.listConsumerGroups().all()
                .thenApply { consumerGroupListings ->
                    consumerGroupListings.map { it.groupId() }
                }
                .thenApply { groupIds ->
                    client.describeConsumerGroups(groupIds).all()
                        .thenApply { consumerGroupDescriptions ->
                            val consumersByTopic = consumerGroupDescriptions.values
                                .flatMap { consumerGroupDescription ->
                                    consumerGroupDescription.members()
                                }
                                .flatMap { memberDescription ->
                                    memberDescription.assignment().topicPartitions().map { it.topic() to memberDescription.consumerId() }
                                }
                                .groupBy({ (topic, _) -> topic }, { (_, consumerId) -> consumerId })
                                .map { (topic, consumerIds) -> topic to HashSet(consumerIds) }
                                .toMap()
                            synchronized(System.out) {
                                println("BEGIN 'Kafka.consumers_by_topic_${server.name}'")
                                server.topics.forEach {
                                    println("SET 'consumers_by_topic_$it' = ${consumersByTopic.getOrDefault(it, emptySet<String>()).size}")
                                }
                                println("END")
                            }
                        }
                }
        }, 0, server.refreshInterval, TimeUnit.SECONDS)
    }
}
