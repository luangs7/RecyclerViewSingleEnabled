package com.lgdevs.recyclerviewwithstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lgdevs.recyclerviewwithstatus.data.DataProvider
import com.lgdevs.recyclerviewwithstatus.model.Item

class MainViewModel : ViewModel() {

    private val _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init {
        _items.postValue(DataProvider.getListOfItems(150))
    }
}