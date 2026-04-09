package uk.ac.wlv.petmate.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.wlv.petmate.data.model.ApiUser
import uk.ac.wlv.petmate.data.repository.AuthRepository

class SessionViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    var currentUser = mutableStateOf<ApiUser?>(null)
        private set

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            currentUser.value = authRepository.getCachedUser()
        }
    }

    fun setUser(user: ApiUser) {
        currentUser.value = user
    }

    fun logout() {
        viewModelScope.launch {
//            authRepository.logout()
            currentUser.value = null
        }
    }
}
