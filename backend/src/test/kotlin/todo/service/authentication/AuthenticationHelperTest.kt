package todo.service.authentication

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * This class holds integration tests for Service
 *
 * */
class AuthenticationHelperTest {
    private val authenticationHelper = AuthenticationHelper()

    @Test
    fun testComputePasswordHash() {
        assertEquals(authenticationHelper.computePasswordHash("test"), "VgEyqh5ghWaqHU0v7xOJtN0eG2zqSO59K4TbFoFf6uM=")
    }
}
