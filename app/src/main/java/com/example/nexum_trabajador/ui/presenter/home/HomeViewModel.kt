package com.example.nexum_trabajador.ui.presenter.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.use_cases.auth.AuthUseCases
import com.example.nexum_trabajador.domain.use_cases.sign_in.SaveLastUserEmailUseCase
import com.example.nexum_trabajador.domain.use_cases.worker.WorkerUseCases
import com.example.nexum_trabajador.security.TokenManager
import com.example.nexum_trabajador.domain.repository.JobOccupationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val workerUseCases: WorkerUseCases,
    private val saveLastUserEmailUseCase: SaveLastUserEmailUseCase,
    private val tokenManager: TokenManager,
    private val jobOccupationRepository: JobOccupationRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
    var signOutJob: Job? = null
    var getProducerJob: Job? = null

    fun getUserId() = tokenManager.getUserId()
    fun getUserIdSync(): Long? = tokenManager.getUserIdSync()

    init {
        Log.d("HomeViewModel", " init ")
        observeWorker()
        Log.d("HomeViewModel", " init 2")
        refreshWorker(fetchFromRemote = true)
        loadOccupations()
    }

    private fun observeWorker() {
        viewModelScope.launch {
            workerUseCases.observeWorker().catch { e ->
                Log.d("HomeViewModel", "Error observando trabajador: ${e.message}")
                state = state.copy(errorMessage = e.message ?: "Unknown error")
            }.collect {
                state = state.copy(worker = it)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.SignOutBtnClicked -> {
                signOut()
            }

            HomeEvent.Refresh -> {
                refreshWorker(fetchFromRemote = true)
            }

            is HomeEvent.AddressChanged -> {
                state = state.copy(worker = state.worker?.copy(address = event.address))
            }

            is HomeEvent.LatitudeChanged -> {
                state = state.copy(worker = state.worker?.copy(latitude = event.latitude))
            }

            is HomeEvent.LongitudeChanged -> {
                state = state.copy(worker = state.worker?.copy(longitude = event.longitude))
            }

            HomeEvent.UpdateWorkerDetails -> {
                updateWorkerDetails()
            }
            HomeEvent.DismissDialog -> {
                state = state.copy(showDialog = false)
            }
            is HomeEvent.ToggleAvailability -> {
                toggleAvailability(event.isAvailable)
            }
        }
    }

    fun toggleAvailability(isAvailable: Boolean) {
        val previousState = state.worker?.isAvailable ?: true
        state = state.copy(
            worker = state.worker?.copy(isAvailable = isAvailable),
            isTogglingAvailability = true
        )
        viewModelScope.launch {
            workerUseCases.setWorkerAvailability(isAvailable)
                .catch { e ->
                    Log.e("HomeViewModel", "Error actualizando disponibilidad: ${e.message}")
                    state = state.copy(
                        worker = state.worker?.copy(isAvailable = previousState),
                        isTogglingAvailability = false,
                        errorMessage = e.message ?: "Error al cambiar disponibilidad"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(isTogglingAvailability = true)
                        }
                        is ApiResponse.Success -> {
                            state = state.copy(
                                isTogglingAvailability = false,
                                worker = state.worker?.copy(isAvailable = response.data.isAvailable ?: isAvailable)
                            )
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                worker = state.worker?.copy(isAvailable = previousState),
                                isTogglingAvailability = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                worker = state.worker?.copy(isAvailable = previousState),
                                isTogglingAvailability = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    fun updateWorkerDetails() {
        viewModelScope.launch {
            workerUseCases.updateWorkerDetails(state.worker!!).catch { e ->
               Log.e("HomeViewModel", "Error actualizando trabajador: ${e.message}")
                state = state.copy(
                    showDialog = true,
                    dialogTitle = "Error",
                    dialogMessage = e.message ?: "Unknown error",
                    isSuccess = false,
                    isUpdating = false
                )
            }.collect {
                when (it) {
                    is ApiResponse.Error -> {
                        state = state.copy(
                            showDialog = true,
                            dialogTitle = "Error",
                            dialogMessage = it.errorMessage,
                            isSuccess = false,
                            isUpdating = false
                        )
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(
                            showDialog = true,
                            dialogTitle = "Error",
                            dialogMessage = it.errorMessage,
                            isSuccess = false,
                            isUpdating = false
                        )

                    }

                    ApiResponse.Loading -> {
                        state = state.copy(isUpdating = true)
                    }

                    is ApiResponse.Success -> {
                        state = state.copy(
                            showDialog = true,
                            dialogTitle = "Éxito",
                            dialogMessage = it.data.message,
                            isSuccess = true,
                            isUpdating = false
                        )
                    }
                }
            }
        }

    }

    fun refreshWorker(fetchFromRemote: Boolean) {
        loadOccupations()
        viewModelScope.launch {
            workerUseCases.updateWorker(fetchFromRemote).catch {
                Log.d("HomeViewModel", "Error actualizando trabajador: ${it.message}")
                state = state.copy(errorMessage = it.message ?: "Unknown error")
            }.collect {
                when (it) {
                    ApiResponse.Loading -> {
                        state = state.copy(isRefreshing = true)
                    }

                    is ApiResponse.Error -> {
                        state = state.copy(isRefreshing = false)
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(isRefreshing = false)
                    }

                    is ApiResponse.Success -> {
                        state = state.copy(isRefreshing = false)
                    }
                }
            }
        }
    }

    fun loadOccupations() {
        viewModelScope.launch {
            state = state.copy(isLoadingOccupations = true)
            jobOccupationRepository.getMyOccupations().catch { e ->
                Log.e("HomeViewModel", "Error cargando profesiones: ${e.message}")
                state = state.copy(isLoadingOccupations = false)
            }.collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state = state.copy(
                            isLoadingOccupations = false,
                            occupations = response.data
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(isLoadingOccupations = false)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(isLoadingOccupations = false)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun signOut() {
        signOutJob = viewModelScope.launch {
            // Guardar el email del usuario actual ANTES de desloguear
//            state.worker?.email?.let {
//                saveLastUserEmailUseCase(it)
//            }

            authUseCases.signOut().collect { response ->
                when (response) {
                    ApiResponse.Loading -> {
                        Log.d("ApiResponse", "Loading")
                        state = state.copy(isSignOutLoading = true)
                    }

                    is ApiResponse.Failure -> {
                        val data = response.errorMessage
                        Log.d("ApiResponse", "errorMessage = $data")
                        state = state.copy(errorMessage = data)
                        state = state.copy(isSignOutLoading = false)
                    }

                    is ApiResponse.Success -> {
                        response.data.let {
                            Log.d("ApiResponse", "message = $it")
//                            authViewModel.onEvent(SplashEvent.CheckAuthentication)
                            state = state.copy(signOutResponse = it)
                        }
                        state = state.copy(isSignOutLoading = false)
                    }

                    is ApiResponse.Error -> TODO()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        signOutJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

        getProducerJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}
