package com.example.labaccess.model.data

data class Laboratory(
    val id: String = "",
    val capacity: Int = 0,
    val description: String = "",
    val name: String = "",
    val assignments: List<Assignment> = listOf(),
){
    override fun toString(): String {
        return name
    }
}
