package com.example.messaging

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.*

@Component
class Consumer {

    private val log = LoggerFactory.getLogger(Consumer::class.java)

    @KafkaListener(
        topics = ["my-first-topic"],
        groupId = "first-consumer"
    )
    fun subcribeMessage(message: String){
        log.info("Message received: {}. At: {}",message, Date().time)
    }

}