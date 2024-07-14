package com.app.efficientworkapp2

data class Activity(
    var title: String,
    var description: String = "Descripción por defecto",
    var isExpanded: Boolean = false
)

data class Item(
    var title: String,
    var description: String = "",
    var isExpanded: Boolean = false,
    var activities: MutableList<Activity> = mutableListOf()
)