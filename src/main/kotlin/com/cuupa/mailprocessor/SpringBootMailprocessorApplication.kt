package com.cuupa.mailprocessor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
open class SpringBootMailprocessorApplication : SpringBootServletInitializer() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootMailprocessorApplication::class.java, *args)
        }
    }
}