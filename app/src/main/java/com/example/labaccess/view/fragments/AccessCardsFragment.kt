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
import com.example.labaccess.view.dialogs.AddAccessCardDialog
import com.example.snapchance.viewModel.AccessCardViewModel


class AccessCardsFragment : Fragment() {

    private lateinit var binding: FragmentAccessCardsBinding

    private val viewModel: AccessCardViewModel by lazy {
        ViewModelProvider(requireActivity())[AccessCardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccessCardsBinding.inflate(inflater, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val teacherId = sharedPreferences.getString("relatedId", null)

        binding.fabNewCard.setOnClickListener{
            // Mostrar diálogo para agregar nueva tarjeta
            val dialog = AddAccessCardDialog()
            dialog.show(parentFragmentManager, "AddAccessCardDialog")
        }

        // Observa los datos desde el ViewModel
        viewModel.teacherAccessCards.observe(viewLifecycleOwner) { accessCards ->
            // Limpia la vista antes de agregar las nuevas filas
            binding.tlAccessCards.removeAllViews()
            // Agregar fila de encabezado
            agregarEncabezado("ID", "Nombre", "Estado")
            // Agregar una fila por cada tarjeta
            Log.d("AccessCardsFragment", "Access cards: $accessCards")
            accessCards.forEach { accessCard ->
                agregarFila(accessCard.cardNumber, accessCard.name, accessCard.state)
            }
        }

        // Cargar las tarjetas de acceso junto con los datos del docente
        viewModel.fecthAllTeacherAccessCards()

        return binding.root
    }

    private fun agregarEncabezado(idTarjeta: String, nombreDocente: String, estado: String) {
        // La fila oscura y el texto en blanco
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } // Crear celdas (TextViews) dinámicamente
        val celdaId = TextView(context).apply {
            text = idTarjeta
            setTextSize(16f)
            setTextColor(resources.getColor(R.color.white))
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        val celdaNombre = TextView(context).apply {
            text = nombreDocente
            setTextSize(16f)
            setTextColor(resources.getColor(R.color.white))
            setPadding(50, 50, 50, 50)
            gravity = Gravity.START
        }
        val celdaEstado = TextView(context).apply {
            text = estado
            setTextSize(16f)
            setTextColor(resources.getColor(R.color.white))
            setPadding(50, 50, 50, 50)
            gravity = Gravity.CENTER
        }
        // Añadir celdas a la fila
        nuevaFila.addView(celdaId)
        nuevaFila.addView(celdaNombre)
        nuevaFila.addView(celdaEstado)
        nuevaFila.setBackgroundColor(resources.getColor(R.color.black))
        // Añadir la fila al TableLayout
        binding.tlAccessCards.addView(nuevaFila)
    }

    private fun agregarFila(idTarjeta: String, nombreDocente: String, estado: String) {
        // Crear una nueva fila
        val nuevaFila = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Crear celdas (TextViews) dinámicamente
        val celdaId = TextView(context).apply {
            text = idTarjeta
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }

        val celdaNombre = TextView(context).apply {
            text = nombreDocente
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.START
        }

        val celdaEstado = TextView(context).apply {
            text = estado
            setTextSize(16f)
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }

        // Añadir celdas a la fila
        nuevaFila.addView(celdaId)
        nuevaFila.addView(celdaNombre)
        nuevaFila.addView(celdaEstado)

        nuevaFila.setBackgroundColor(resources.getColor(R.color.gray))

        // Añadir la fila al TableLayout
        binding.tlAccessCards.addView(nuevaFila)
    }

}
