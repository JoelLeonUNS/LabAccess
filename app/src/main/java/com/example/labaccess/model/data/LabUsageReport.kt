package com.example.labaccess.model.data

data class LabUsageReport(
    var labName: String = "",
    var teacherName: String = "",
    var entryTime: String = "",
    var exitTime: String = "",
    var usageDate: String = "" // Nuevo campo para la fecha
)


