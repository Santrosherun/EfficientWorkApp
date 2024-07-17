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
        val updatedList = (workerList.value ?: mutableListOf()).toMutableList()
        updatedList.add(worker)
        workerList.value = updatedList
        saveItemsToPreferences()
    }

    fun updateActivity(parentItem: Item, updatedActivity: Activity) {
        val updatedItemList = (_itemList.value ?: emptyList()).toMutableList()
        val itemIndex = updatedItemList.indexOfFirst { it.title == parentItem.title }

        if (itemIndex != -1) {
            val updatedItem = updatedItemList[itemIndex]
            val activityIndex = updatedItem.activities.indexOfFirst { it.title == updatedActivity.title }

            if (activityIndex != -1) {
                updatedItem.activities[activityIndex] = updatedActivity
                _itemList.value = updatedItemList
                saveItemsToPreferences()
            }
        }
    }

    fun removeItem(item: Item) {
        val updatedList = (_itemList.value ?: emptyList()).toMutableList()
        updatedList.remove(item)
        _itemList.value = updatedList
        saveItemsToPreferences()
    }

    fun removeWorker(worker: String) {
        val updatedList = (workerList.value ?: mutableListOf()).toMutableList()
        updatedList.remove(worker)
        workerList.value = updatedList

        // Remover el trabajador de todas las actividades
        val updatedItemList = (_itemList.value ?: emptyList()).toMutableList()
        updatedItemList.forEach { item ->
            item.activities.forEach { activity ->
                activity.workers.remove(worker)
            }
        }
        _itemList.value = updatedItemList

        saveItemsToPreferences()
    }

    private fun saveItemsToPreferences() {
        // Guardar datos en SharedPreferences
        val itemsJson = gson.toJson(_itemList.value)
        sharedPreferences.edit().putString("items_key", itemsJson).apply()

        val workersJson = gson.toJson(workerList.value)
        sharedPreferences.edit().putString("workers_key", workersJson).apply()
    }

    private fun loadItemsFromPreferences() {
        // Cargar datos desde SharedPreferences
        val json = sharedPreferences.getString("items_key", null)
        val itemType = object : TypeToken<List<Item>>() {}.type
        val items: List<Item> = gson.fromJson(json, itemType) ?: emptyList()
        _itemList.value = items

        val workersJson = sharedPreferences.getString("workers_key", null)
        val workerType = object : TypeToken<MutableList<String>>() {}.type
        val workers: MutableList<String> = gson.fromJson(workersJson, workerType) ?: mutableListOf()
        workerList.value = workers
    }
}