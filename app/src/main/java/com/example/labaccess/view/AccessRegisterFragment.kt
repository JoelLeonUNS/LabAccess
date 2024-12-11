package com.example.labaccess.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labaccess.R
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.labaccess.model.data.AccessLog
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.Teacher
import com.example.labaccess.model.repo.AccessLogRepository
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AccessRegisterFragment : Fragment() {

    private lateinit var btnSimulateScan: MaterialButton
    private lateinit var tableLogs: TableLayout
    private lateinit var btnRegisterAccess: MaterialButton  // Botón para registrar el acceso

    private var selectedTeacher: Teacher? = null
    private var selectedLab: Laboratory? = null

    private var entryTimeMillis: Long = 0  // Para almacenar la hora de entrada en milisegundos
    private var exitTimeMillis: Long = 0  // Para almacenar la hora de salida en milisegundos

    private val accessLogRepository = AccessLogRepository() // Instanciamos el repositorio

    private var clickCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_access_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de vistas
        btnSimulateScan = view.findViewById(R.id.btnSimulateScan)
        tableLogs = view.findViewById(R.id.tableLogs)
        btnRegisterAccess = view.findViewById(R.id.btnRegisterAccess)  // Inicializamos el botón de registrar acceso

        // Configuración del botón para simular el escaneo
        btnSimulateScan.setOnClickListener {
            if (clickCount == 0) {
                // Primer clic: Mostrar la hora de entrada
                entryTimeMillis = System.currentTimeMillis()
                showSelectedValuesInTable(entryTimeMillis, null) // Mostramos la hora de entrada
                clickCount++
            } else if (clickCount == 1) {
                // Segundo clic: Mostrar la hora de salida
                exitTimeMillis = System.currentTimeMillis()
                showSelectedValuesInTable(entryTimeMillis, exitTimeMillis) // Mostrar la hora de entrada y salida
                clickCount = 0
                selectRandomTeacherAndLab() // Seleccionamos nuevos valores aleatorios para el siguiente clic
            }
        }

        // Configuración del botón para registrar el acceso
        btnRegisterAccess.setOnClickListener {
            registerAccessLog()
        }

        // Selección inicial aleatoria de Teacher y Lab
        selectRandomTeacherAndLab()
    }

    // Método para seleccionar aleatoriamente un docente y un laboratorio
    private fun selectRandomTeacherAndLab() {
        val teachersRef = FirebaseFirestore.getInstance().collection("teachers")
        val labsRef = FirebaseFirestore.getInstance().collection("laboratories")

        teachersRef.get().addOnSuccessListener { teachersSnapshot ->
            val teachers = teachersSnapshot.toObjects(Teacher::class.java)
            selectedTeacher = teachers.randomOrNull()
        }

        labsRef.get().addOnSuccessListener { labsSnapshot ->
            val labs = labsSnapshot.toObjects(Laboratory::class.java)
            selectedLab = labs.randomOrNull()
        }
    }

    // Método para mostrar los valores seleccionados en la tabla
    private fun showSelectedValuesInTable(entryTime: Long?, exitTime: Long?) {
        val row = TableRow(context)

        row.addView(createTextView(selectedTeacher?.name ?: "No Docente"))
        row.addView(createTextView(selectedLab?.name ?: "No Laboratorio"))
        row.addView(createTextView(entryTime?.let { formatTimestamp(it) } ?: "N/A"))  // Mostrar fecha y hora de entrada
        row.addView(createTextView(exitTime?.let { formatTimestamp(it) } ?: "N/A"))   // Mostrar fecha y hora de salida

        tableLogs.addView(row)
    }

    // Método auxiliar para formatear el timestamp en un formato legible
    private fun formatTimestamp(timestamp: Long): String {
        // Formato incluye fecha y hora
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    // Método para crear un TextView con padding
    private fun createTextView(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.setPadding(16, 8, 16, 8)
        return textView
    }

    // Método para registrar el acceso
    private fun registerAccessLog() {
        if (selectedTeacher == null || selectedLab == null) {
            Toast.makeText(context, "Datos incompletos. No se puede registrar el acceso.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el último TableRow de la tabla
        val lastRow = tableLogs.getChildAt(tableLogs.childCount - 1) as TableRow

        // Obtener los valores de la última fila
        val teacherName = (lastRow.getChildAt(0) as TextView).text.toString()
        val labName = (lastRow.getChildAt(1) as TextView).text.toString()
        val entryTimeText = (lastRow.getChildAt(2) as TextView).text.toString()
        val exitTimeText = (lastRow.getChildAt(3) as TextView).text.toString()

        // Convertir las horas de entrada y salida de texto a Timestamp
        val entryTimestamp = convertToTimestamp(entryTimeText)
        val exitTimestamp = convertToTimestamp(exitTimeText)

        // Buscar los documentos de Teacher y Lab por nombre
        val teacherRef = FirebaseFirestore.getInstance().collection("teachers")
            .whereEqualTo("name", teacherName).get()

        val labRef = FirebaseFirestore.getInstance().collection("laboratories")
            .whereEqualTo("name", labName).get()

        teacherRef.addOnSuccessListener { teacherSnapshot ->
            val teacherDocument = teacherSnapshot.documents.firstOrNull()
            val teacherDocumentReference = teacherDocument?.reference

            labRef.addOnSuccessListener { labSnapshot ->
                val labDocument = labSnapshot.documents.firstOrNull()
                val labDocumentReference = labDocument?.reference

                if (teacherDocumentReference != null && labDocumentReference != null) {
                    // Crear el objeto AccessLog
                    val accessLog = AccessLog(
                        entryTime = entryTimestamp,
                        exitTime = exitTimestamp,
                        labId = labDocumentReference,
                        teacherId = teacherDocumentReference
                    )

                    // Guardar el AccessLog en Firestore
                    lifecycleScope.launch {
                        val success = accessLogRepository.addAccessLog(accessLog)
                        if (success) {
                            Toast.makeText(context, "Registro guardado correctamente.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al guardar el registro.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "No se encontraron los documentos para el docente o laboratorio.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para convertir un tiempo en formato "yyyy-MM-dd HH:mm:ss" a un Timestamp
    private fun convertToTimestamp(time: String): Timestamp {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(time)
        return Timestamp(date)
    }
}