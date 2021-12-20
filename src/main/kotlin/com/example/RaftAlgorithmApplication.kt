package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RaftAlgorithmApplication

fun main(args: Array<String>) {
	runApplication<RaftAlgorithmApplication>(*args)
}
