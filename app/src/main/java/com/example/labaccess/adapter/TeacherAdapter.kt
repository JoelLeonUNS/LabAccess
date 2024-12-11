package com.example.labaccess.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labaccess.R
import com.example.labaccess.model.data.Teacher

class TeacherAdapter(private val teachers: List<Teacher>) :
    RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.teacherNameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.teacherEmailTextView)
        private val stateTextView: TextView = itemView.findViewById(R.id.teacherStateTextView)

        fun bind(teacher: Teacher) {
            nameTextView.text = teacher.name
            emailTextView.text = teacher.email
            stateTextView.text = teacher.state
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher_row, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.bind(teachers[position])
    }

    override fun getItemCount(): Int = teachers.size
}
