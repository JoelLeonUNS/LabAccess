package com.example.labaccess.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labaccess.R
import com.example.labaccess.adapter.LabUsageReportAdapter
import com.example.labaccess.databinding.FragmentLabUseBinding
import com.example.labaccess.model.data.LabUsageReport
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.viewmodel.LabUseViewModel
import java.util.Calendar

class LabUseFragment : Fragment() {

    private lateinit var binding: FragmentLabUseBinding
    private val labUseViewModel: LabUseViewModel by lazy {
        ViewModelProvider(requireActivity())[LabUseViewModel::class.java]
    }

    private lateinit var labUsageReportAdapter: LabUsageReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLabUseBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        val recyclerView = binding.rvTable
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        labUseViewModel.labs.observe(viewLifecycleOwner, Observer { labs ->
            // Crear un adaptador para el Spinner con los nombres de los laboratorios
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labs.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerLab.adapter = adapter
        })

        // Cargar los laboratorios disponibles
        labUseViewModel.getLaboratories()

        // Configurar el DatePicker para el campo Fecha Inicio
        binding.etStartDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.etStartDate.setText(selectedDate)
            }
        }

        // Configurar el DatePicker para el campo Fecha Fin
        binding.etEndDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.etEndDate.setText(selectedDate)
            }
        }



        // Observar los reportes de uso de laboratorio
        labUseViewModel.labUsageReports.observe(viewLifecycleOwner) { reports ->
            if (reports.isNotEmpty()) {
                Log.d("LabUsageFragment", "Mostrando reportes en RecyclerView")
                val adapter = LabUsageReportAdapter(reports)
                binding.rvTable.adapter = adapter
            } else {
                Log.d("LabUsageFragment", "No hay reportes para mostrar")
            }
        }


        // Filtrar los reportes
        binding.btnFilter.setOnClickListener {
            val startDate = binding.etStartDate.text.toString()
            val endDate = binding.etEndDate.text.toString()
            val selectedLabName = binding.spinnerLab.selectedItem.toString()

            val selectedLab = labUseViewModel.labs.value?.find { it.name == selectedLabName }
            if (selectedLab != null) {
                val labId = selectedLab.id
                Log.d("LabUseReport", "Datos enviados: ${startDate} , ${endDate} , ${labId}")
                labUseViewModel.getLabUsageReports(startDate, endDate, labId)
            } else {
                Toast.makeText(requireContext(), "Laboratorio no encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Función para mostrar el DatePickerDialog
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Formatear la fecha seleccionada
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
