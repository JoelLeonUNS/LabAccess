package com.example.labaccess.model.data

data class Teacher(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val state: String = "",
    val accessCard: List<AccessCard> = listOf(),
) {
    override fun toString(): String {
        return name // Retorna solo el nombre
    }
}