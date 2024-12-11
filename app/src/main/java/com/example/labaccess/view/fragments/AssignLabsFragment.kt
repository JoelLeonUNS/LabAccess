package com.example.labaccess.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.example.labaccess.model.data.LaboratoryJoin
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
            agregarEncabezado("Lab", "Docente")
            for (lab in laboratoryJoin) {
                agregarFila(lab)
            }
        }

        // Cargar las tarjetas de acceso junto con los datos del docente
        viewModel.fecthAllLaboratoryJoin()

        return binding.root
    }

    private fun agregarEncabezado(laboratory:String, teacher:String) {
        // La fila oscura y el texto en blanco
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val celdaLaboratory = TextView(context).apply {
            text = laboratory
            setTextSize(16f)
            setTextColor(resources.getColor(R.color.white))
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        val celdaTeacher = TextView(context).apply {
            text = teacher
            setTextSize(16f)
            setTextColor(resources.getColor(R.color.white))
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        // Añadir celdas a la fila
        nuevaFila.addView(celdaLaboratory)
        nuevaFila.addView(celdaTeacher)
        nuevaFila.setBackgroundColor(resources.getColor(R.color.black))
        // Añadir la fila al TableLayout
        binding.tlAssignLabs.addView(nuevaFila)
    }

    private fun agregarFila(lab:LaboratoryJoin) {
        // Crear una nueva fila
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val celdaLaboratory = TextView(context).apply {
            text = lab.name
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        val celdaTeacher = TextView(context).apply {
            text = lab.teacher
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        // Añadir celdas a la fila
        nuevaFila.addView(celdaLaboratory)
        nuevaFila.addView(celdaTeacher)

        nuevaFila.setOnClickListener {
            mostrarDialogoDetalle(lab)
        }

        // Añadir la fila al TableLayout
        binding.tlAssignLabs.addView(nuevaFila)
    }

    private fun mostrarDialogoDetalle(lab: LaboratoryJoin) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Detalles de la Asignación")
        builder.setMessage(
            """
        Horario: ${lab.timeSlot}
        Día de la semana: ${lab.dayOfWeek}
        Laboratorio: ${lab.name}
        Curso: ${lab.course}
        Profesor: ${lab.teacher}
        Información adicional: ${lab.description}
        """.trimIndent()
        )
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

}
