package serializers

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertFails

internal class LocalDateTimeIso8601SerializerTest {

    @Serializable
    data class SerializableTestDate(@Serializable(with = LocalDateTimeIso8601Serializer::class) var date: LocalDateTime)

    @Test
    fun testSerialize() {
        val iso1806dateString = "2013-06-01T20:19:44"
        val date = LocalDateTime.parse(iso1806dateString)

        assertEquals("{\"date\":\"$iso1806dateString\"}", Json.encodeToString(SerializableTestDate.serializer(), SerializableTestDate(date)))
    }

    @Test
    fun testDeserialize() {
        val iso1806dateString = "2013-06-01T20:19:44"
        val serializableTestDateString = "{\"date\":\"$iso1806dateString\"}"

        assertEquals(LocalDateTime.parse(iso1806dateString), Json.decodeFromString(SerializableTestDate.serializer(), serializableTestDateString).date)
    }

    @Test
    fun testDeserializeNonISO1806() {
        val iso1806dateString = "2013/06/01 20:19:44"
        val serializableTestDateString = "{\"date\":\"$iso1806dateString\"}"

        assertFails {
            Json.decodeFromString(SerializableTestDate.serializer(), serializableTestDateString)
        }
    }
}
