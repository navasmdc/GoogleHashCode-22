package com.beerbucket

data class Dev(
    val name: String,
    val skills: MutableMap<String, Int> = mutableMapOf(),
    var assignments: MutableList<Pair<Int, Int>> = mutableListOf()
    ) {
    val lastBusyDay: Int
        get() = assignments.maxBy { it.second }?.second ?: 0
}
