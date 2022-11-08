package clipboard

import models.Item

/**
 * Clipboard class to save items on the clipboard for hotkey support.
 */
class Clipboard {
    private var savedItem: Item? = null

    fun saveItem(item: Item) {
        savedItem = item
    }

    fun getSavedItem(): Item? {
        return savedItem
    }
}
