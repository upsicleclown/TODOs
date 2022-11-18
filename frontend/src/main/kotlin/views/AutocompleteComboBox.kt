package views

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class AutocompleteComboBox : ComboBox<String>() {
    private var invertedAutoCompleteItems: ObservableList<String> = FXCollections.observableArrayList()
    private val NAVIGATION_KEYS = listOf(KeyCode.ENTER, KeyCode.ESCAPE, KeyCode.DOWN, KeyCode.UP, KeyCode.LEFT, KeyCode.RIGHT)

    init {
        /* region styling */
        visibleRowCount = 4
        styleClass.addAll("combobox", "label-max")
        editor.styleClass.addAll("label-max")
        /* end region styling */

        /* region event filters */
        addEventFilter(
            KeyEvent.KEY_RELEASED
        ) { keyPress ->

            /* don't refresh matches if we are navigating keys */
            if (keyPress.code in NAVIGATION_KEYS) return@addEventFilter

            /* autocomplete filter */
            for (item in (items + invertedAutoCompleteItems)) {
                if (autocompleteFilter(item) && item in items) {
                    invertedAutoCompleteItems.remove(item)
                } else if (autocompleteFilter(item) && item in invertedAutoCompleteItems) {
                    items.add(item)
                    invertedAutoCompleteItems.remove(item)
                } else if (!autocompleteFilter(item) && item in items) {
                    items.remove(item)
                    invertedAutoCompleteItems.add(item)
                }
            }

            /* reset items */
            if (items.size > 0) {
                val cleanedItems = items.sorted().distinct()
                items.setAll(cleanedItems)
            }

            /* refresh visibleRowCount if necessary */
            if (items.size <= 4 && items.size != visibleRowCount) {
                visibleRowCount = items.size
                this.hide()
            } else if (items.size > 4 && visibleRowCount < 4) {
                visibleRowCount = 4
                this.hide()
            }
            this.show()
        }
        /* end region event filters */
    }

    private fun autocompleteFilter(item: String): Boolean {
        val editorText = editor.text.trim()
        return item.startsWith(editorText, ignoreCase = true)
    }
}
