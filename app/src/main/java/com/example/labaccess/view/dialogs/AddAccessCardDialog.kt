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
import com.example.labaccess.model.data.Teacher
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
        val adminId = sharedPreferences.getString("relatedId", null)
        var teacherId = ""

        viewModel.cardNumber.observe(viewLifecycleOwner) { cardNumber ->
            binding.etCardNumber.setText(cardNumber)
        }

        // Observa los datos desde el ViewModel
        vmTeacher.teachers.observe(viewLifecycleOwner) { teachers ->
            // Configura el adaptador para el Spinner utilizando directamente los objetos Teacher
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                teachers // Pasa la lista completa de objetos
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTeacher.adapter = adapter
        }

        // Configura el listener para obtener el elemento seleccionado
        binding.spinnerTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Recupera el objeto Teacher seleccionado utilizando la posición
                val selectedTeacher = parent.getItemAtPosition(position) as Teacher
                // Actualiza el id del profesor seleccionado
                teacherId = selectedTeacher.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Maneja el caso en que no se selecciona nada
            }
        }

        vmTeacher.fetchAllTeachers()

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                viewModel.resetResult()
                viewModel.clearCurrentItem()
                viewModel.fecthAllTeacherAccessCards()
                dismiss()
            }
            if(success == false) {
                Toast.makeText(requireContext(),"Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.updateCardNumber(binding.etCardNumber.text.toString())
            viewModel.saveAccessCard(teacherId)
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