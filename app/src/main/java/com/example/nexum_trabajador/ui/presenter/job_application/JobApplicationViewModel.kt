package com.example.nexum_trabajador.ui.presenter.job_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddJobApplicationReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddMaterialReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddTaskReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.TimeRangeReq
import com.example.nexum_trabajador.domain.model.JobApplicationDraft
import com.example.nexum_trabajador.domain.use_cases.job_application.ApplyToJobOfferUseCase
import com.example.nexum_trabajador.domain.use_cases.job_application.GetJobApplicationDraftUseCase
import com.example.nexum_trabajador.domain.use_cases.job_application.SaveJobApplicationDraftUseCase
import com.example.nexum_trabajador.domain.use_cases.job_application.DeleteJobApplicationDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class JobApplicationState(
    val jobOfferId: Long = 0,
    val jobOfferUuid: String = "",
    val scheduledDate: String = "",
    val estimatedTimeInMinutes: Double = 0.0,
    val laborCost: Double = 0.0,
    val workWarrantyInDays: Int = 0,
    val proposalText: String = "",
    val materials: List<AddMaterialReq> = emptyList(),
    val tasks: List<AddTaskReq> = emptyList(),
    val workSchedules: Map<String, List<TimeRangeReq>> = emptyMap(),
    
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class JobApplicationViewModel @Inject constructor(
    private val applyToJobOfferUseCase: ApplyToJobOfferUseCase,
    private val getJobApplicationDraftUseCase: GetJobApplicationDraftUseCase,
    private val saveJobApplicationDraftUseCase: SaveJobApplicationDraftUseCase,
    private val deleteJobApplicationDraftUseCase: DeleteJobApplicationDraftUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(JobApplicationState())
    val state: StateFlow<JobApplicationState> = _state.asStateFlow()

    fun init(jobOfferId: Long, jobOfferUuid: String) {
        if (_state.value.jobOfferId == 0L) {
            _state.update { it.copy(jobOfferId = jobOfferId, jobOfferUuid = jobOfferUuid) }
            loadDraft(jobOfferId)
        }
    }

    private fun loadDraft(jobOfferId: Long) {
        viewModelScope.launch {
            val draft = getJobApplicationDraftUseCase(jobOfferId)
            if (draft != null) {
                val materialsType = object : com.google.gson.reflect.TypeToken<List<AddMaterialReq>>() {}.type
                val tasksType = object : com.google.gson.reflect.TypeToken<List<AddTaskReq>>() {}.type
                val schedulesType = object : com.google.gson.reflect.TypeToken<Map<String, List<TimeRangeReq>>>() {}.type

                val loadedMaterials: List<AddMaterialReq> = try {
                    Gson().fromJson(draft.materialsJson, materialsType)
                } catch (e: Exception) { emptyList() }

                val loadedTasks: List<AddTaskReq> = try {
                    Gson().fromJson(draft.tasksJson, tasksType)
                } catch (e: Exception) { emptyList() }

                val loadedSchedules: Map<String, List<TimeRangeReq>> = try {
                    Gson().fromJson(draft.workSchedulesJson, schedulesType)
                } catch (e: Exception) { emptyMap() }

                _state.update {
                    it.copy(
                        proposalText = draft.proposalText,
                        laborCost = draft.laborCost,
                        workWarrantyInDays = draft.workWarrantyInDays,
                        materials = loadedMaterials,
                        tasks = loadedTasks,
                        workSchedules = loadedSchedules
                    )
                }
                updateSchedule(loadedSchedules)
            }
        }
    }

    private fun saveCurrentDraft() {
        val s = _state.value
        if (s.jobOfferId != 0L) {
            val draft = JobApplicationDraft(
                jobOfferId = s.jobOfferId,
                jobOfferUuid = s.jobOfferUuid,
                proposalText = s.proposalText,
                laborCost = s.laborCost,
                workWarrantyInDays = s.workWarrantyInDays,
                materialsJson = Gson().toJson(s.materials),
                tasksJson = Gson().toJson(s.tasks),
                workSchedulesJson = Gson().toJson(s.workSchedules)
            )
            viewModelScope.launch {
                saveJobApplicationDraftUseCase(draft)
            }
        }
    }

    fun updateProposalAndCost(proposal: String, cost: Double, warranty: Int) {
        _state.update {
            it.copy(
                proposalText = proposal,
                laborCost = cost,
                workWarrantyInDays = warranty
            )
        }
        saveCurrentDraft()
    }

    fun addMaterial(material: AddMaterialReq) {
        _state.update { it.copy(materials = it.materials + material) }
        saveCurrentDraft()
    }

    fun removeMaterial(index: Int) {
        _state.update {
            val newList = it.materials.toMutableList()
            if (index in newList.indices) newList.removeAt(index)
            it.copy(materials = newList)
        }
        saveCurrentDraft()
    }

    fun editMaterial(index: Int, updated: AddMaterialReq) {
        _state.update {
            val newList = it.materials.toMutableList()
            if (index in newList.indices) newList[index] = updated
            it.copy(materials = newList)
        }
        saveCurrentDraft()
    }

    fun addTask(task: AddTaskReq) {
        _state.update { it.copy(tasks = it.tasks + task) }
        saveCurrentDraft()
    }

    fun removeTask(index: Int) {
        _state.update {
            val newList = it.tasks.toMutableList()
            if (index in newList.indices) newList.removeAt(index)
            it.copy(tasks = newList)
        }
        saveCurrentDraft()
    }

    fun editTask(index: Int, updated: AddTaskReq) {
        _state.update {
            val newList = it.tasks.toMutableList()
            if (index in newList.indices) newList[index] = updated
            it.copy(tasks = newList)
        }
        saveCurrentDraft()
    }

    fun updateSchedule(schedules: Map<String, List<TimeRangeReq>>) {
        val totalHours = schedules.values.sumOf { it.size }
        val estimatedTime = totalHours * 60.0

        val isoScheduledDate = if (schedules.isNotEmpty()) {
            try {
                // Find earliest date key (yyyy-MM-dd sorts chronologically)
                val earliestDateStr = schedules.keys.minOrNull() ?: ""
                val ranges = schedules[earliestDateStr] ?: emptyList()
                val earliestTimeStr = ranges.map { it.startTime }.minOrNull() ?: "08:00"

                val date = java.time.LocalDate.parse(earliestDateStr)
                val time = java.time.LocalTime.parse(earliestTimeStr)

                java.time.LocalDateTime.of(date, time)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toOffsetDateTime()
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            } catch (e: Exception) {
                java.time.OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }
        } else {
            java.time.OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        _state.update {
            it.copy(
                workSchedules = schedules,
                scheduledDate = isoScheduledDate,
                estimatedTimeInMinutes = estimatedTime
            )
        }
        saveCurrentDraft()
    }

    fun submitApplication() {
        val currentState = _state.value
        val req = AddJobApplicationReq(
            jobOfferId = currentState.jobOfferId,
            scheduledDate = currentState.scheduledDate,
            estimatedTimeInMinutes = currentState.estimatedTimeInMinutes,
            laborCost = currentState.laborCost,
            workWarrantyInDays = currentState.workWarrantyInDays,
            proposalText = currentState.proposalText,
            materials = currentState.materials,
            tasks = currentState.tasks,
            workSchedules = currentState.workSchedules
        )

        _state.update { it.copy(isSubmitting = true, errorMessage = "") }

        viewModelScope.launch {
            val result = applyToJobOfferUseCase(req)
            result.onSuccess {
                deleteJobApplicationDraftUseCase(currentState.jobOfferId)
                _state.update { it.copy(isSubmitting = false, isSuccess = true) }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun dismissError() {
        _state.update { it.copy(errorMessage = "") }
    }
}
