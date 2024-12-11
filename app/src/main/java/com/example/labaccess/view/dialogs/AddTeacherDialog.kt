package com.example.labaccess.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.labaccess.databinding.DialogAddTeacherBinding
import com.example.snapchance.viewModel.TeacherViewModel

class AddTeacherDialog: DialogFragment() {

    private lateinit var binding: DialogAddTeacherBinding
    private val viewModel: TeacherViewModel by lazy {
        ViewModelProvider(requireActivity())[TeacherViewModel::class.java]}

    interface DialogListener {
        fun onDialogSubmit(input: String)
    }

    var listener: DialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTeacherBinding.inflate(inflater, container, false)

        viewModel.name.observe(viewLifecycleOwner) { name ->
            binding.nombreInput.setText(name ?: "")
        }
        viewModel.email.observe(viewLifecycleOwner) { email ->
            binding.correoInput.setText(email ?: "")
        }
        viewModel.password.observe(viewLifecycleOwner) { password ->
            binding.passwordInput.setText(password ?: "")
        }

        viewModel.createResult.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Docente creado correctamente", Toast.LENGTH_SHORT).show()
                viewModel.resetCreateResult()
                dismiss()
            }
            if(success == false) {
                Toast.makeText(requireContext(), "Error al crear docente", Toast.LENGTH_SHORT).show()
            }

        }


        binding.btnSave.setOnClickListener {
            val name = binding.nombreInput.text?.toString() ?: ""
            val email = binding.correoInput.text?.toString() ?: ""
            val password = binding.passwordInput.text?.toString() ?: ""

            viewModel.updateName(name)
            viewModel.updateEmail(email)
            viewModel.updatePassword(password)
            viewModel.updateState("Activo")
            viewModel.addTeacher()

        }


        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}