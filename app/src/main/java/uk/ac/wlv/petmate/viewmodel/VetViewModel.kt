package uk.ac.wlv.petmate.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.wlv.petmate.core.UiState
import uk.ac.wlv.petmate.core.utils.safeApiCall
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.repository.VetRepository

class VetViewModel(private val vetRepository: VetRepository,) : BaseViewModel() {



    private val accumulatedVets = mutableListOf<Vet>()

    private val _vetListState = MutableStateFlow<UiState<List<Vet>>>(UiState.Idle)
    val vetListState: StateFlow<UiState<List<Vet>>> = _vetListState

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _selectedVetState = MutableStateFlow<UiState<Vet>>(UiState.Idle)
    val selectedVetState: StateFlow<UiState<Vet>> = _selectedVetState.asStateFlow()

    init {
        loadVetList()
    }

    fun loadVetList() {
        viewModelScope.launch {
            _vetListState.value = UiState.Loading
            accumulatedVets.clear()

            val result = safeApiCall { vetRepository.getVetList(isRefresh = true) }

            result.onSuccess { vets ->
                accumulatedVets.addAll(vets)
                _vetListState.value = UiState.Success(accumulatedVets.toList())
            }.onFailure { exception ->
                _vetListState.value = UiState.Error(exception.message ?: "Failed to load vets")
            }
        }
    }

    fun loadNextPage() {
        if (_isLoadingMore.value || vetRepository.isLastPage) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            val result = safeApiCall { vetRepository.getVetList() }

            result.onSuccess { vets ->
                accumulatedVets.addAll(vets)
                _vetListState.value = UiState.Success(accumulatedVets.toList())
            }.onFailure { exception ->
                _vetListState.value = UiState.Error(exception.message ?: "Failed to load more vets")
            }

            _isLoadingMore.value = false
        }
    }

    fun refresh() = loadVetList()

    fun loadVet(vetId: Int) {
        viewModelScope.launch {
            _selectedVetState.value = UiState.Loading
            val result = safeApiCall {vetRepository.getVet( vetId)}
            result.onSuccess { vet -> _selectedVetState.value = UiState.Success(vet) }.onFailure {
                    exception ->
                _selectedVetState.value = UiState.Error(exception.message ?: "Failed to load Vet")
            }
        }
    }
}