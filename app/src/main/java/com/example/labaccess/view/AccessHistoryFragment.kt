package com.example.labaccess.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.labaccess.R
import com.example.labaccess.model.data.AccessLog
import com.example.labaccess.viewmodel.AccessHistoryViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.*

class AccessHistoryFragment : Fragment() {

    private lateinit var viewModel: AccessHistoryViewModel
    private lateinit var startDateEditText: TextInputEditText
    private lateinit var endDateEditText: TextInputEditText
    private lateinit var accessLogsTable: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_access_history, container, false)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[AccessHistoryViewModel::class.java]

        // Configurar observadores para el ViewModel
        setupObservers(view)

        // Obtener referencias a los EditText para las fechas
        startDateEditText = view.findViewById(R.id.start_date)
        endDateEditText = view.findViewById(R.id.end_date)

        // Configurar el DatePicker para las fechas de inicio y fin
        setupDatePicker(startDateEditText)
        setupDatePicker(endDateEditText)

        // Referencia al TableLayout para mostrar los registros
        accessLogsTable = view.findViewById(R.id.records_table)

        return view
    }

    private fun setupObservers(view: View) {
        val teacherSpinner = view.findViewById<Spinner>(R.id.teacher_spinner)
        val labSpinner = view.findViewById<Spinner>(R.id.lab_spinner)

        // Observar la lista de docentes
        viewModel.teachers.observe(viewLifecycleOwner) { teachers ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                teachers.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            teacherSpinner.adapter = adapter
        }

        // Observar la lista de laboratorios
        viewModel.laboratory.observe(viewLifecycleOwner) { laboratories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                laboratories.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            labSpinner.adapter = adapter
        }

        // Observar los registros de acceso
        viewModel.accessLogs.observe(viewLifecycleOwner) { logs ->
            updateTable(logs)  // Actualizar la tabla con los registros filtrados
        }
    }

    private fun setupDatePicker(editText: TextInputEditText) {
        editText.setOnClickListener {
            // Obtener la fecha actual como predeterminada
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Mostrar el DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                    editText.setText(formattedDate)

                    // Convertir la fecha seleccionada a Timestamp
                    val calendar = Calendar.getInstance()
                    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
                    val timestamp = Timestamp(calendar.time)

                    // Filtrar los logs después de seleccionar las fechas
                    filterAccessLogs()  // Llamar a la función para actualizar los registros
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }


    private fun filterAccessLogs() {
        // Obtener las fechas seleccionadas en los EditText
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()

        // Convertir las fechas seleccionadas a Timestamp
        val startTimestamp = startDate.toDateTimestamp()
        val endTimestamp = endDate.toDateTimestamp()

        // Llamar al ViewModel para filtrar los registros
        viewModel.filterAccessLogs(startTimestamp, endTimestamp)
    }

    // Extensión para convertir un String con formato dd/MM/yyyy a Timestamp
    private fun String.toDateTimestamp(): Timestamp? {
        return try {
            val format = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.parse(this)
            date?.let { Timestamp(it) }
        } catch (e: Exception) {
            null
        }
    }

    private fun updateTable(logs: List<AccessLog>) {
        // Limpiar la tabla antes de agregar las nuevas filas
        accessLogsTable.removeAllViews()

        // Iterar sobre los registros de acceso y agregar filas a la tabla
        logs.forEach { log ->
            val tableRow = TableRow(requireContext())
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            // Crear las celdas para cada campo del log
            val entryTimeTextView = TextView(requireContext()).apply {
                text = log.entryTime.toString()  // Mostrar la hora de entrada
                setPadding(8, 8, 8, 8)
            }

            val exitTimeTextView = TextView(requireContext()).apply {
                text = log.exitTime.toString()  // Mostrar la hora de salida
                setPadding(8, 8, 8, 8)
            }

            // Obtener los datos del laboratorio usando el ViewModel
            val labIdTextView = TextView(requireContext()).apply {
                log.labId?.let { labRef ->
                    viewModel.getLaboratoryData(labRef).observe(viewLifecycleOwner) { laboratory ->
                        text = laboratory?.name ?: "Desconocido"
                    }
                }
                setPadding(8, 8, 8, 8)
            }

            // Obtener los datos del docente usando el ViewModel
            val teacherIdTextView = TextView(requireContext()).apply {
                log.teacherId?.let { teacherRef ->
                    viewModel.getTeacherData(teacherRef).observe(viewLifecycleOwner) { teacher ->
                        text = teacher?.name ?: "Desconocido"
                    }
                }
                setPadding(8, 8, 8, 8)
            }

            // Agregar las celdas a la fila
            tableRow.addView(entryTimeTextView)
            tableRow.addView(exitTimeTextView)
            tableRow.addView(labIdTextView)
            tableRow.addView(teacherIdTextView)

            // Agregar la fila a la tabla
            accessLogsTable.addView(tableRow)
        }
    }
}

