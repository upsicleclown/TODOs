package controllers

import java.util.*

interface ItemsViewController {

    private val items = ArrayList<Items>()

    fun addItem() {}

    fun deleteItem(item: Item) {}

    fun renderGroup(group: Group) {}

    fun addLabelToItem(item: Item) {}

    fun changeItemColor(item: Item) {}

    fun addView(view: IView) {
    }
}