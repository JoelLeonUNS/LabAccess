package com.example.labaccess.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labaccess.R
import com.example.labaccess.adapter.TeacherAdapter
import com.example.labaccess.databinding.FragmentUsersManagementBinding
import com.example.labaccess.view.dialogs.AddTeacherDialog
import com.example.snapchance.viewModel.TeacherViewModel

class UsersManagementFragment : Fragment() {

    private lateinit var binding: FragmentUsersManagementBinding
    private val viewModel: TeacherViewModel by lazy {
        ViewModelProvider(requireActivity())[TeacherViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersManagementBinding.inflate(inflater, container, false)

        // Configurar el RecyclerView
        val recyclerView = binding.recyclerViewAdmins
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observa los docentes en el ViewModel
        viewModel.teachers.observe(viewLifecycleOwner) { teachers ->
            if (teachers != null) {
                val adapter = TeacherAdapter(teachers)
                recyclerView.adapter = adapter
            }
        }

        // Cargar datos de docentes
        viewModel.fetchAllTeachers()

        binding.btnAddAdmin.setOnClickListener {
            val dialogFragment = AddTeacherDialog()
            dialogFragment.show(parentFragmentManager, "AddTeacherDialogFragment")
        }
        return  binding.root
    }

}