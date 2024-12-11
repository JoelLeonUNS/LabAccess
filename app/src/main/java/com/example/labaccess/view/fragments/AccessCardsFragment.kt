package com.example.labaccess.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.labaccess.R
import com.example.labaccess.databinding.FragmentAccessCardsBinding
import com.example.labaccess.view.dialogs.AddAccessCardDialog


class AccessCardsFragment : Fragment() {

    private lateinit var binding: FragmentAccessCardsBinding

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

        // Agregar filas iniciales
        agregarFila("Juan", "Pérez", "Activa")
        agregarFila("María", "Gómez", "Inactiva")

        return binding.root
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
            setPadding(8, 8, 8, 8)
            gravity = Gravity.CENTER
        }

        val celdaNombre = TextView(context).apply {
            text = nombreDocente
            setPadding(8, 8, 8, 8)
            gravity = Gravity.START
        }

        val celdaEstado = TextView(context).apply {
            text = estado
            setPadding(8, 8, 8, 8)
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
