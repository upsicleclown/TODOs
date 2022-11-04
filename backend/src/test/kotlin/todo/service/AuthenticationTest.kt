package todo.service

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * This class holds integration tests for Service
 *
 * */
class AuthenticationTest {
    private val authentication = Authentication()

    @Test
    fun testComputePasswordHash() {
        assertEquals(authentication.computePasswordHash("test"), "VgEyqh5ghWaqHU0v7xOJtN0eG2zqSO59K4TbFoFf6uM=")
    }
}
