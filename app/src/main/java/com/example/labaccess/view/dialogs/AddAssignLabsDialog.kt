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
import com.example.labaccess.databinding.DialogAddAssignLabsBinding
import com.example.labaccess.model.data.Course
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.Teacher
import com.example.snapchance.viewModel.AccessCardViewModel
import com.example.snapchance.viewModel.CourseViewModel
import com.example.snapchance.viewModel.LaboratoryViewModel
import com.example.snapchance.viewModel.TeacherViewModel

class AddAssignLabsDialog: DialogFragment() {

    private lateinit var binding: DialogAddAssignLabsBinding
    private val viewModel: LaboratoryViewModel by lazy {
        ViewModelProvider(requireActivity())[LaboratoryViewModel::class.java]
    }
    private val vmTeacher: TeacherViewModel by lazy {
        ViewModelProvider(requireActivity())[TeacherViewModel::class.java]
    }
    private val vmCourse: CourseViewModel by lazy {
        ViewModelProvider(requireActivity())[CourseViewModel::class.java]
    }

    interface DialogListener {
        fun onDialogSubmit(input: String)
    }

    var listener: DialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddAssignLabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val adminId = sharedPreferences.getString("relatedId", null)
        var teacherId = ""
        var courseId = ""
        var laboratoryId = ""
        var timeSlot = ""

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

        // Observa los datos desde el ViewModel
        vmCourse.courses.observe(viewLifecycleOwner) { courses ->
            // Configura el adaptador para el Spinner utilizando directamente los objetos Course
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                courses // Pasa la lista completa de objetos
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCourse.adapter = adapter
        }

        // Configura el listener para obtener el elemento seleccionado
        binding.spinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Recupera el objeto Course seleccionado utilizando la posición
                val selectedCourse = parent.getItemAtPosition(position) as Course
                // Actualiza el id del curso seleccionado
                courseId = selectedCourse.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Maneja el caso en que no se selecciona nada
            }
        }

        // Observa los datos desde el ViewModel
        viewModel.assignments.observe(viewLifecycleOwner) { laboratories ->
            // Configura el adaptador para el Spinner utilizando directamente los objetos Assignment
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                laboratories // Pasa la lista completa de objetos
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerLaboratory.adapter = adapter
        }

        // Configura el listener para obtener el elemento seleccionado
        binding.spinnerLaboratory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Recupera el objeto Laboratory seleccionado utilizando la posición
                val selectedLaboratory = parent.getItemAtPosition(position) as Laboratory
                // Actualiza el id del laboratorio seleccionado
                laboratoryId = selectedLaboratory.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Maneja el caso en que no se selecciona nada
            }
        }

        // Obtener el horario seleccionado
        timeSlot = binding.spinnerTimeSlot.selectedItem.toString()

        vmTeacher.fetchAllTeachers()
        vmCourse.fecthAllCourses()
        viewModel.fecthAllLaboratories()

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                viewModel.resetResult()
                viewModel.clearCurrentItem()
                viewModel.fecthAllLaboratoryJoin()
                dismiss()
            }
            if(success == false) {
                Toast.makeText(requireContext(),"Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.updateTimeSlot(timeSlot)
            viewModel.updateTeacherId(teacherId)
            viewModel.updateCourseId(courseId)
            viewModel.updateId(laboratoryId)
            viewModel.saveAssignment()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(): AddAssignLabsDialog {
            return AddAssignLabsDialog()
        }
    }
}