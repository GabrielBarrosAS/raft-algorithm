package com.example.messaging

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class Producer (
    private val kafkaTemplate: KafkaTemplate<String, String>
){

    fun publishMessage(message: String): String {
        val newMessage = "$message - ${Date().time}"
        kafkaTemplate.send("my-first-topic", newMessage)
        return message;
    }

}