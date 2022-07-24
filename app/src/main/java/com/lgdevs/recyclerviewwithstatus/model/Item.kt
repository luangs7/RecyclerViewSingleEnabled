package com.lgdevs.recyclerviewwithstatus.model

data class Item(
    var id: String,
    var name: String,
    var isSelected: Boolean = false,
    var isEnabled: Boolean = false
)