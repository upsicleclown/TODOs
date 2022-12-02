package cache

import models.WindowSettings
import theme.Theme
import kotlin.test.Test
import kotlin.test.assertEquals

class CacheTest {
    private val cache = Cache(
        this.javaClass.classLoader.getResource("cache/window_settings.json")!!.path,
        this.javaClass.classLoader.getResource("cache/group_id_to_item_id_ordering.json")!!.path
    )

    /*
       Tests related to window settings.
     */
    @Test
    fun testEditWindowSettings() {
        val newX = 419.0
        val newY = 70.0
        val newHeight = 600.0
        val newWidth = 400.0
        val newTheme = Theme.DARK

        cache.editWindowSettings(WindowSettings(newX, newY, newHeight, newWidth, newTheme))
        val newWindowSettings = cache.getWindowSettings()
        assertEquals(newWindowSettings.x, newX)
        assertEquals(newWindowSettings.y, newY)
        assertEquals(newWindowSettings.height, newHeight)
        assertEquals(newWindowSettings.width, newWidth)
        assertEquals(newWindowSettings.theme, newTheme)
    }

    @Test
    fun testGetWindowSettings() {
        val expectedPosition = 0.0
        val expectedSize = 500.0

        val windowSettings = cache.getWindowSettings()
        assertEquals(windowSettings.x, expectedPosition)
        assertEquals(windowSettings.y, expectedPosition)
        assertEquals(windowSettings.height, expectedSize)
        assertEquals(windowSettings.width, expectedSize)
    }

    /*
       Tests related to item ordering
     */
    @Test
    fun testEditItemOrdering() {
        val expectedNumberOfCachedOrdering = 2
        val newGroupToItemOrdering: HashMap<Int, List<Int>> = HashMap()
        val expectedGroup1 = 1
        val expectedGroup1ItemIdOrdering = listOf(2, 1, 3)
        newGroupToItemOrdering[expectedGroup1] = expectedGroup1ItemIdOrdering
        val expectedGroup2 = 2
        val expectedGroup2ItemIdOrdering = listOf(4, 3)
        newGroupToItemOrdering[expectedGroup2] = expectedGroup2ItemIdOrdering

        cache.editGroupToItemOrdering(newGroupToItemOrdering)
        val newItemOrdering = cache.getGroupToItemOrdering()
        assertEquals(newItemOrdering.size, expectedNumberOfCachedOrdering)
        assertEquals(newItemOrdering[expectedGroup1], expectedGroup1ItemIdOrdering)
        assertEquals(newItemOrdering[expectedGroup2], expectedGroup2ItemIdOrdering)
    }

    @Test
    fun testGetItemOrdering() {
        val expectedNumberOfCachedOrdering = 1
        val expectedItemIdOrdering = listOf(1, 2, 3)

        val groupIdToItemIdOrdering = cache.getGroupToItemOrdering()
        assertEquals(groupIdToItemIdOrdering.size, expectedNumberOfCachedOrdering)
        assertEquals(groupIdToItemIdOrdering[1], expectedItemIdOrdering)
    }
}
