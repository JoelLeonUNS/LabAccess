package com.example.labaccess.model.data

data class Laboratories(
    val id: String = "",
    val capacity: Int = 0,
    val description: String = "",
    val name: String = "",
    val accessCard: List<Assignments> = listOf(),
)
