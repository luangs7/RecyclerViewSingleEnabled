package com.lgdevs.recyclerviewwithstatus.data

import com.lgdevs.recyclerviewwithstatus.model.Item

object DataProvider {

    fun getListOfItems(quantity: Int): MutableList<Item> {
        val items = mutableListOf<Item>()
        for (i in 0 until quantity){
            items.add(Item(i.toString(), "Teste $i"))
        }

        return items
    }
}