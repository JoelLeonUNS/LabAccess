package com.example.labaccess.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labaccess.R
import com.example.labaccess.databinding.FragmentLabUseBinding
import java.util.Calendar

class LabUseFragment : Fragment() {

    private lateinit var binding: FragmentLabUseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLabUseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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