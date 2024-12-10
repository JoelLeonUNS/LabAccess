package com.example.labaccess.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labaccess.R
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class AccessRegisterFragment : Fragment() {

    private lateinit var etNfcId: EditText
    private lateinit var tableLogs: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar la vista del fragmento
        val view = inflater.inflate(R.layout.fragment_access_register, container, false)

        etNfcId = view.findViewById(R.id.etNfcId)
        tableLogs = view.findViewById(R.id.tableLogs)
        val btnSimulateScan: Button = view.findViewById(R.id.btnSimulateScan)
        val btnRegisterAccess: Button = view.findViewById(R.id.btnRegisterAccess)

        // Configurar los botones
        btnSimulateScan.setOnClickListener { simulateNFCScan() }
        btnRegisterAccess.setOnClickListener { registerAccess() }

        return view
    }

    private fun simulateNFCScan() {
        // Generar un ID NFC aleatorio
        val simulatedId = "ID" + (10000..99999).random()
        etNfcId.setText(simulatedId)
    }

    private fun registerAccess() {
        val nfcId = etNfcId.text.toString()

        if (nfcId.isEmpty()) {
            Toast.makeText(context, "Por favor, escanee una tarjeta NFC", Toast.LENGTH_SHORT).show()
            return
        }

        // Agregar un nuevo registro a la tabla
        val row = TableRow(context)
        row.addView(createTextView("Nombre del Docente"))
        row.addView(createTextView("Lab A"))
        row.addView(createTextView(SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault()).format(Date())))
        row.addView(createTextView("En curso"))

        tableLogs.addView(row)
        etNfcId.text.clear() // Limpiar el campo después del registro
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.setPadding(8, 8, 8, 8)
        return textView
    }
}
