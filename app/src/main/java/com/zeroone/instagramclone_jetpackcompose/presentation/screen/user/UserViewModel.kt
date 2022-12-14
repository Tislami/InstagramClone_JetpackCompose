package com.zeroone.instagramclone_jetpackcompose.presentation.screen.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.instagramclone_jetpackcompose.domain.model.Response
import com.zeroone.instagramclone_jetpackcompose.domain.model.User
import com.zeroone.instagramclone_jetpackcompose.domain.use_case.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UseCase,
) : ViewModel() {

    private val _userState = mutableStateOf(UserState())
    val userState = _userState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isLoading = mutableStateOf(false)
    private var job: Job? = null

    private fun setUser(user: User) {
        Log.d("AppAuth", "user_viewModel_set: set init")
        job?.cancel()
        job = viewModelScope.launch {
            useCase.userUseCase.setUser(user).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.d("AppAuth", "user_viewModel_set: error ${response.message}")
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        Log.d("AppAuth", "user_viewModel_set: success result ${response.data.id}")
                        _userState.value = userState.value.copy(user = response.data)
                        _eventFlow.emit(UIEvent.Set)
                    }
                }
            }
        }
    }

    private fun checkUserState(id: String) {
        Log.d("AppAuth", "user_viewModel_check: init")
        job?.cancel()
        job = viewModelScope.launch {
            useCase.userUseCase.getUser(id = id).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.d("AppAuth", "user_viewModel_check: error ${response.message}")
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        if (response.data != null) {
                            Log.d(
                                "AppAuth",
                                "user_viewModel_check: success result ${response.data.id}"
                            )
                            _userState.value = userState.value.copy(user = response.data)
                            _eventFlow.emit(UIEvent.Set)
                        } else {
                            _eventFlow.emit(UIEvent.NotCompleted)
                            Log.d("AppAuth", "user_viewModel_check: success result not completed")
                        }
                    }
                }
            }
        }
    }

    private fun getUser() {
        Log.d("AppAuth", "user_viewModel_get: init")
        job?.cancel()
        job = viewModelScope.launch {
            useCase.userUseCase.getUser(userState.value.user.id).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.d("AppAuth", "user_viewModel_get: error ${response.message}")
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        if (response.data != null) {
                            Log.d(
                                "AppAuth",
                                "user_viewModel_get: success result ${response.data.id}"
                            )
                            _userState.value = userState.value.copy(user = response.data)
                            getUserPosts()
                        } else {
                            _eventFlow.emit(UIEvent.Error("Unknown Error"))
                            Log.d("AppAuth", "user_viewModel_get: success but Unknown Error")
                        }
                    }
                }
            }
        }
    }

    /* private fun singOut() {
         viewModelScope.launch {
             useCase.userUseCase.singOut().collect { response ->
                 when (response) {
                     is Response.Error -> {
                         isLoading.value = false
                         _eventFlow.emit(UIEvent.Error(response.message))
                     }
                     is Response.Loading -> { isLoading.value = true }
                     is Response.Success -> {
                         _eventFlow.emit(UIEvent.SignOut)
                         isLoading.value = false
                     }
                 }
             }
         }
     }*/

    private fun getUserPosts() {
        Log.d("PostApp", "user_viewModel_getUserPosts: init")

        job?.cancel()
        job = viewModelScope.launch {
            useCase.postUseCase.getUserPosts(userState.value.user.id).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.d(
                            "PostApp",
                            "user_viewModel_getUserPosts: response error ${response.message}"
                        )
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        Log.d(
                            "PostApp",
                            "user_viewModel_getUserPosts: response success ${response.data.size}"
                        )
                        isLoading.value = false
                        _userState.value = userState.value.copy(
                            posts = response.data
                        )
                    }
                }
            }
        }
    }


    private fun getFollowers() {
        viewModelScope.launch {
            useCase.userUseCase.getFollow(userState.value.user.followers)
                .collect { response ->
                when (response) {
                    is Response.Error -> {
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        _userState.value = userState.value.copy(
                            followers = response.data
                        )
                    }
                }
            }
        }
    }

    private fun getFollowing(){
        viewModelScope.launch {
            useCase.userUseCase.getFollow(userState.value.user.following).collect { response ->
                when (response) {
                    is Response.Error -> {
                        isLoading.value = false
                        _eventFlow.emit(UIEvent.Error(response.message))
                    }
                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        _userState.value = userState.value.copy(
                            following = response.data
                        )
                    }
                }
            }
        }
    }


    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SetUser -> setUser(event.user)
            is UserEvent.CheckUserState -> checkUserState(event.userId)
            is UserEvent.GetUserPosts -> getUserPosts()
            is UserEvent.GetFollowers -> getFollowers()
            is UserEvent.GetFollowing -> getFollowing()
            is UserEvent.GetUser -> getUser()
            UserEvent.SingOut -> {}
        }
    }

    sealed class UIEvent {
        data class Error(val message: String) : UIEvent()
        object NotCompleted : UIEvent()
        object Set : UIEvent()
        object SignOut : UIEvent()

    }

}