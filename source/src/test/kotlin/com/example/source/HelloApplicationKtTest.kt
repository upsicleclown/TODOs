package com.example.source

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class HelloApplicationKtTest {

    @Test
    fun generateGreeting() {
        val expected = "Hello world!"
        assertEquals(expected, generateGreeting("world"))
    }
}