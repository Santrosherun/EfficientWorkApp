package com.app.efficientworkapp2

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _itemList = MutableLiveData<MutableList<Item>>() // Lista mutable para almacenar los datos
    val itemList: LiveData<MutableList<Item>> get() = _itemList // Exponer la lista como LiveData

    private val sharedPreferences = application.getSharedPreferences("project_prefs", Context.MODE_PRIVATE) // SharedPreferences para almacenar los datos
    private val gson = Gson() // Serializar y deserializar datos?

    init { // Inicializador encargado de cargar los datos
        // Cargar datos desde SharedPreferences al iniciar
        loadItemsFromPreferences()
    }

    fun addItem(item: Item) { // Agregar un nuevo item
        _itemList.value?.add(item)
        _itemList.value = _itemList.value // Notificar cambio
        saveItemsToPreferences()
    }

    private fun saveItemsToPreferences() {
        // Guardar datos en SharedPreferences
        val itemsJson = gson.toJson(_itemList.value)
        sharedPreferences.edit().putString("items_key", itemsJson).apply()
    }

    private fun loadItemsFromPreferences() {
        // Cargar datos desde SharedPreferences
        val itemsJson = sharedPreferences.getString("items_key", null)
        // Verificar si hay datos
        if (itemsJson != null) {
            // Deserializar datos
            val itemType = object : TypeToken<MutableList<Item>>() {}.type
            val items: MutableList<Item> = gson.fromJson(itemsJson, itemType)
            _itemList.value = items
        } else {
            _itemList.value = mutableListOf()
        }
    }
}