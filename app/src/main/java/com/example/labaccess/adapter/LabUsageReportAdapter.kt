package com.example.labaccess.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labaccess.R
import com.example.labaccess.model.data.LabUsageReport

class LabUsageReportAdapter(private var reports: List<LabUsageReport>) : RecyclerView.Adapter<LabUsageReportAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labName: TextView = itemView.findViewById(R.id.tvLabName)
        val teacherId: TextView = itemView.findViewById(R.id.tvTeacherName)
        val entryTime: TextView = itemView.findViewById(R.id.tvEntryTime)
        val exitTime: TextView = itemView.findViewById(R.id.tvExitTime)
        val usageData: TextView = itemView.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lab_usage_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.labName.text = ("Laboratorio: " + report.labName) ?: "Laboratorio no disponible"
        holder.teacherId.text = ("Docente: " + report.teacherName) ?: "Profesor no disponible"
        holder.entryTime.text = ("Hora de entrada: " + report.entryTime) ?: "Sin hora de entrada"
        holder.exitTime.text = ("Hora de salida: " + report.exitTime) ?: "Sin hora de salida"
        holder.usageData.text = ("Fecha: " + report.usageDate) ?: "Sin fecha"
    }

    override fun getItemCount(): Int = reports.size

    fun updateData(newReports: List<LabUsageReport>) {
        reports = newReports
        notifyDataSetChanged()
    }
}



