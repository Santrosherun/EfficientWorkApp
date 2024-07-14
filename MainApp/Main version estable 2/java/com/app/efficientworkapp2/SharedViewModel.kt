package com.app.efficientworkapp2

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _itemList = MutableLiveData<List<Item>>() // Lista mutable para almacenar los datos
    val itemList: LiveData<List<Item>> get() = _itemList // Exponer la lista como LiveData
    val workerList = MutableLiveData<MutableList<String>>(mutableListOf())

    private val sharedPreferences = application.getSharedPreferences("project_prefs", Context.MODE_PRIVATE) // SharedPreferences para almacenar los datos
    private val gson = Gson() // Serializar y deserializar datos?

    init { // Inicializador encargado de cargar los datos
        // Cargar datos desde SharedPreferences al iniciar
        loadItemsFromPreferences()
    }

    fun addItem(item: Item) {
        val updatedList = (_itemList.value ?: emptyList()).toMutableList()
        updatedList.add(item)
        _itemList.value = updatedList
        saveItemsToPreferences()
    }

    fun updateItem(item: Item) {
        val updatedList = (_itemList.value ?: emptyList()).toMutableList()
        val index = updatedList.indexOfFirst { it.title == item.title }
        if (index != -1) {
            updatedList[index] = item
            _itemList.value = updatedList
            saveItemsToPreferences()
        }
    }

    fun addWorker(worker: String) {
        workerList.value?.add(worker)
        workerList.postValue(workerList.value)
    }

    fun removeItem(item: Item) {
        val updatedList = (_itemList.value ?: emptyList()).toMutableList()
        updatedList.remove(item)
        _itemList.value = updatedList
        saveItemsToPreferences()
    }

    private fun saveItemsToPreferences() {
        // Guardar datos en SharedPreferences
        val itemsJson = gson.toJson(_itemList.value)
        sharedPreferences.edit().putString("items_key", itemsJson).apply()
    }

    private fun loadItemsFromPreferences() {
        // Cargar datos desde SharedPreferences
        val json = sharedPreferences.getString("items_key", null)
        val itemType = object : TypeToken<List<Item>>() {}.type
        val items: List<Item> = gson.fromJson(json, itemType) ?: emptyList()
        _itemList.value = items
    }
}