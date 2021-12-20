package com.example.controller

import com.example.messaging.Producer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
class MessageController(
    private val producer: Producer
) {

    @PostMapping
    fun publish(@RequestBody message: String): String {
        return producer.publishMessage(message);
    }

}