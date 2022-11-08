package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun testConstructor() {
        val expectedUsername = "marco"
        val expectedPassword = "password!"

        val user = User(expectedUsername, expectedPassword)
        assertEquals(user.username, expectedUsername)
        assertEquals(user.password, expectedPassword)
    }
}
