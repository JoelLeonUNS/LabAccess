package com.example.labaccess.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.labaccess.R
import java.text.SimpleDateFormat
import java.util.*

class AccessHistoryFragment : Fragment() {

    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var teacherSpinner: Spinner
    private lateinit var labSpinner: Spinner
    private lateinit var filterButton: Button
    private lateinit var recordsTable: TableLayout

    private val records = mutableListOf(
        Record("Juan Pérez", "Lab A", "2023-05-01 09:00", "2023-05-01 10:30"),
        Record("María García", "Lab B", "2023-05-01 11:00", "2023-05-01 12:30")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_access_history, container, false)

        startDateEditText = view.findViewById(R.id.start_date)
        endDateEditText = view.findViewById(R.id.end_date)
        teacherSpinner = view.findViewById(R.id.teacher_spinner)
        labSpinner = view.findViewById(R.id.lab_spinner)
        filterButton = view.findViewById(R.id.btn_filter)
        recordsTable = view.findViewById(R.id.records_table)

        setupDatePickers()
        setupSpinners()
        setupButtons()
        populateTable(records)

        return view
    }

    private fun setupDatePickers() {
        val datePickerListener = { editText: EditText ->
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    editText.setText(format.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        startDateEditText.setOnClickListener { datePickerListener(startDateEditText) }
        endDateEditText.setOnClickListener { datePickerListener(endDateEditText) }
    }

    private fun setupSpinners() {
        val teachers = listOf("Todos", "Juan Pérez", "María García")
        val labs = listOf("Todos", "Lab A", "Lab B")

        teacherSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            teachers
        )

        labSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            labs
        )
    }

    private fun setupButtons() {
        filterButton.setOnClickListener { filterRecords() }
    }

    private fun filterRecords() {
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()
        val selectedTeacher = teacherSpinner.selectedItem.toString()
        val selectedLab = labSpinner.selectedItem.toString()

        val filteredRecords = records.filter {
            (startDate.isEmpty() || it.entryTime >= startDate) &&
                    (endDate.isEmpty() || it.entryTime <= endDate) &&
                    (selectedTeacher == "Todos" || it.teacherName == selectedTeacher) &&
                    (selectedLab == "Todos" || it.lab == selectedLab)
        }

        populateTable(filteredRecords)
    }


    private fun populateTable(records: List<Record>) {
        recordsTable.removeViews(1, recordsTable.childCount - 1) // Elimina filas anteriores

        records.forEach { record ->
            val row = TableRow(requireContext())

            val teacherCell = TextView(requireContext())
            teacherCell.text = record.teacherName
            teacherCell.setPadding(8, 8, 8, 8)

            val labCell = TextView(requireContext())
            labCell.text = record.lab
            labCell.setPadding(8, 8, 8, 8)

            val entryCell = TextView(requireContext())
            entryCell.text = record.entryTime
            entryCell.setPadding(8, 8, 8, 8)

            val exitCell = TextView(requireContext())
            exitCell.text = record.exitTime
            exitCell.setPadding(8, 8, 8, 8)

            row.addView(teacherCell)
            row.addView(labCell)
            row.addView(entryCell)
            row.addView(exitCell)

            recordsTable.addView(row)
        }
    }

    data class Record(
        val teacherName: String,
        val lab: String,
        val entryTime: String,
        val exitTime: String
    )
}
