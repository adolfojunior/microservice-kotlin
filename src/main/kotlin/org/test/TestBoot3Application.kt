package org.test

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TestBoot3Application {
}

fun main(args: Array<String>) {
	SpringApplication.run(TestBoot3Application::class.java, *args)
}
