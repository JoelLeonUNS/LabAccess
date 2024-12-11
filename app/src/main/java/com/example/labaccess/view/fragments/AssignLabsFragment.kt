package com.example.labaccess.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.labaccess.R
import com.example.labaccess.databinding.FragmentAccessCardsBinding
import com.example.labaccess.databinding.FragmentAssignLabsBinding
import com.example.labaccess.view.dialogs.AddAccessCardDialog
import com.example.labaccess.view.dialogs.AddAssignLabsDialog
import com.example.snapchance.viewModel.AccessCardViewModel
import com.example.snapchance.viewModel.LaboratoryViewModel


class AssignLabsFragment : Fragment() {

    private lateinit var binding: FragmentAssignLabsBinding

    private val viewModel: LaboratoryViewModel by lazy {
        ViewModelProvider(requireActivity())[LaboratoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignLabsBinding.inflate(inflater, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val teacherId = sharedPreferences.getString("relatedId", null)

        binding.fabNewCard.setOnClickListener{
            // Mostrar diálogo para agregar nueva tarjeta
            val dialog = AddAssignLabsDialog()
            dialog.show(parentFragmentManager, "AddAssignLabsDialog")
        }

        // Observa los datos desde el ViewModel
        viewModel.laboratoryJoin.observe(viewLifecycleOwner) { laboratoryJoin ->
            binding.tlAssignLabs.removeAllViews()
            agregarEncabezado("Horario", "Laboratorio", "Curso", "Docente")
            for (lab in laboratoryJoin) {
                agregarFila(lab.timeSlot, lab.name, lab.course, lab.teacher)
            }
        }

        // Cargar las tarjetas de acceso junto con los datos del docente
        viewModel.fecthAllLaboratoryJoin()

        return binding.root
    }

    private fun agregarEncabezado(timeSlot:String, laboratory:String, course:String, teacher:String) {
        // La fila oscura y el texto en blanco
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } // Crear celdas (TextViews) dinámicamente
        val celdaTimeSlot = TextView(context).apply {
            text = timeSlot
            setTextSize(16f)
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        val celdaLaboratory = TextView(context).apply {
            text = laboratory
            setTextSize(16f)
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        val celdaCourse = TextView(context).apply {
            text = course
            setTextSize(16f)
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        val celdaTeacher = TextView(context).apply {
            text = teacher
            setTextSize(16f)
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        // Añadir celdas a la fila
        nuevaFila.addView(celdaTimeSlot)
        nuevaFila.addView(celdaLaboratory)
        nuevaFila.addView(celdaCourse)
        nuevaFila.addView(celdaTeacher)
        nuevaFila.setBackgroundColor(resources.getColor(R.color.black))
        // Añadir la fila al TableLayout
        binding.tlAssignLabs.addView(nuevaFila)
    }

    private fun agregarFila(timeSlot:String, laboratory:String, course:String, teacher:String) {
        // Crear una nueva fila
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        // Crear celdas (TextViews) dinámicamente
        val celdaTimeSlot = TextView(context).apply {
            text = timeSlot
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }
        val celdaLaboratory = TextView(context).apply {
            text = laboratory
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }
        val celdaCourse = TextView(context).apply {
            text = course
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }
        val celdaTeacher = TextView(context).apply {
            text = teacher
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }
        // Añadir celdas a la fila
        nuevaFila.addView(celdaTimeSlot)
        nuevaFila.addView(celdaLaboratory)
        nuevaFila.addView(celdaCourse)
        nuevaFila.addView(celdaTeacher)

        // Añadir la fila al TableLayout
        binding.tlAssignLabs.addView(nuevaFila)
    }

}
