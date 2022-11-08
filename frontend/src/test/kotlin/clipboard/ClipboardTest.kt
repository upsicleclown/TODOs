package clipboard

import models.Item
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClipboardTest {
    private val clipboard = Clipboard()

    @Test
    fun testSavingItem() {
        val expectedItem = Item("item1", false, id = 1)
        assertNull(clipboard.getSavedItem())
        clipboard.saveItem(expectedItem)
        assertEquals(expectedItem, clipboard.getSavedItem())
    }
}
