package uk.ac.wlv.petmate.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.wlv.petmate.core.UiState
import uk.ac.wlv.petmate.core.utils.safeApiCall
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.model.VetFilterState
import uk.ac.wlv.petmate.data.repository.VetRepository

class VetViewModel(private val vetRepository: VetRepository,) : BaseViewModel() {



    private val accumulatedVets = mutableListOf<Vet>()

    private val _vetListState = MutableStateFlow<UiState<List<Vet>>>(UiState.Idle)
    val vetListState: StateFlow<UiState<List<Vet>>> = _vetListState

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filterState = MutableStateFlow(VetFilterState())
    val filterState: StateFlow<VetFilterState> = _filterState

    private val _selectedVetState = MutableStateFlow<UiState<Vet>>(UiState.Idle)
    val selectedVetState: StateFlow<UiState<Vet>> = _selectedVetState.asStateFlow()

    init {
        loadVetList(isRefresh = true)
    }

    fun loadVetList(isRefresh: Boolean = false) {
        viewModelScope.launch {

            if (isRefresh) {
                accumulatedVets.clear()
                _vetListState.value = UiState.Loading
            } else {
                _isLoadingMore.value = true
            }

            val result = safeApiCall {
                vetRepository.getVetList(
                    isRefresh   = isRefresh,
                    filter      = _filterState.value,
                    searchQuery = _searchQuery.value
                )
            }

            result.onSuccess { vets ->

                if (isRefresh) {
                    accumulatedVets.clear()
                }

                accumulatedVets.addAll(vets)

                _vetListState.value = UiState.Success(accumulatedVets.toList())

            }.onFailure { exception ->
                _vetListState.value = UiState.Error(exception.message ?: "Failed to load vets")
            }

            _isLoadingMore.value = false
        }
    }

    fun applyFilter(filter: VetFilterState) {
        _filterState.value = filter
        loadVetList(isRefresh = true)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        loadVetList(isRefresh = true)
    }

    fun loadMoreVets() {
        if (_isLoadingMore.value) return

        loadVetList(isRefresh = false)
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