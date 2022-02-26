package com.beerbucket

data class Project(
    val name: String,
    val days: Int,
    val score: Int,
    val bestDay: Int,
    val skills: MutableList<Pair<String, Int>> = mutableListOf()
)
