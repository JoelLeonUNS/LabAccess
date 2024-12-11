package com.example.labaccess.view.dialogs

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.labaccess.databinding.DialogAddAccessCardBinding
import com.example.snapchance.viewModel.AccessCardViewModel
import com.example.snapchance.viewModel.TeacherViewModel

class AddAccessCardDialog: DialogFragment() {

    private lateinit var binding: DialogAddAccessCardBinding
    private val viewModel: AccessCardViewModel by lazy {
        ViewModelProvider(requireActivity())[AccessCardViewModel::class.java]
    }
    private val vmTeacher: TeacherViewModel by lazy {
        ViewModelProvider(requireActivity())[TeacherViewModel::class.java]
    }

    interface DialogListener {
        fun onDialogSubmit(input: String)
    }

    var listener: DialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddAccessCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val tearcherId = sharedPreferences.getString("relatedId", null)

        viewModel.cardNumber.observe(viewLifecycleOwner) { cardNumber ->
            binding.etCardNumber.setText(cardNumber)
        }

        // Observa los datos desde el ViewModel
        vmTeacher.teachers.observe(viewLifecycleOwner) { teachers ->
            // Convierte los objetos Teacher a una lista de nombres (o el atributo que quieras mostrar)
            val teacherNames = teachers.map { it.name } // Cambia 'name' según tu modelo

            // Configura el adaptador para el Spinner
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                teacherNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTeacher.adapter = adapter
        }

        vmTeacher.fetchAllTeachers()

        // Configura el listener para obtener el elemento seleccionado
        binding.spinnerTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTeacher = parent.getItemAtPosition(position) as String
                // Aquí puedes trabajar con el nombre seleccionado o buscar el objeto completo
                Log.d("Spinner", "Seleccionado: $selectedTeacher")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Aquí puedes manejar el caso en que no se seleccione nada, si es necesario
            }
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                viewModel.resetResult()
                viewModel.clearCurrentItem()
                viewModel.fetchAccessCards(tearcherId!!)
                dismiss()
            }
            if(success == false) {
                Toast.makeText(requireContext(),"Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.updateCardNumber(binding.etCardNumber.text.toString())
            viewModel.saveAccessCard(tearcherId!!)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(): AddAccessCardDialog {
            return AddAccessCardDialog()
        }
    }
}