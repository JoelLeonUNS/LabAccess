package com.example.labaccess.model.data

data class Course(
    val id: String = "",
    val name: String = "",
    val description: String = "",
) {
    override fun toString(): String {
        return name // Retorna solo el nombre
    }
}