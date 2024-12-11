package com.example.labaccess.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.labaccess.model.data.LabUsageReport
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.repo.LabUseRepository

class LabUseViewModel(application: Application) : AndroidViewModel(application) {

    private val labUseRepository = LabUseRepository()

    private val _labUsageReports = MutableLiveData<List<LabUsageReport>>()
    val labUsageReports: LiveData<List<LabUsageReport>> get() = _labUsageReports

    private val _labs = MutableLiveData<List<Laboratory>>()
    val labs: LiveData<List<Laboratory>> get() = _labs

    // Función para obtener los laboratorios
    fun getLaboratories() {
        labUseRepository.getLaboratory { labList ->
            _labs.postValue(labList)
        }
    }

    // Función para obtener los reportes de uso filtrados
    fun getLabUsageReports(startDate: String, endDate: String, labId: String) {
        Log.d("LabUseReport", "Datos a punto de enviar: Start: $startDate, End: $endDate, LabId: $labId")
        labUseRepository.getLabUsageReports(startDate, endDate, labId) { reports ->
            if (reports.isNotEmpty()) {
                Log.d("LabUseReport", "Reportes obtenidos: $reports")
                _labUsageReports.postValue(reports)
            } else {
                Log.d("LabUseReport", "No se encontraron reportes.")
            }
        }
    }

}


