package com.example.pupilmesh.Ui_Layer.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pupilmesh.Common.Routes.Routes
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import com.example.pupilmesh.Domain_Layer.Modal.User
import com.example.pupilmesh.Domain_Layer.UseCases.CheckUserCredentialsUseCase
import com.example.pupilmesh.Domain_Layer.UseCases.GetAllMangaUseCase
import com.example.pupilmesh.Domain_Layer.UseCases.GetLoginStatus
import com.example.pupilmesh.Domain_Layer.UseCases.GetMangaUseCase
import com.example.pupilmesh.Domain_Layer.UseCases.InsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PupilVm @Inject constructor(
    private  val insertUserUseCase: InsertUserUseCase,
    private  val credentialsUseCase: CheckUserCredentialsUseCase,
    private val getLoginStatus: GetLoginStatus,
    private val getAllMangaUseCase: GetAllMangaUseCase,
    private val getMangaUseCase: GetMangaUseCase
):ViewModel() {

    private val _startDestination = MutableStateFlow<String>(Routes.LoginRoute.routes)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isLoggedIn = getLoginStatus().first()
            Log.d("DATASTOREVM", "isLoggedIn : $isLoggedIn")
            _startDestination.value = if (isLoggedIn) Routes.HomeRoute.routes else Routes.LoginRoute.routes
        }
    }


    private val _insertState = MutableStateFlow<State<Unit>>(State.Loading())
    val insertState: StateFlow<State<Unit>> = _insertState.asStateFlow()

    fun insertUser(email : String , password :String) {
        viewModelScope.launch {
            _insertState.value = State.Loading()
            _insertState.value = insertUserUseCase.invoke(User(email, password))
        }
    }



    private val _loginState = MutableStateFlow<State<Boolean>>(State.Loading())
    val loginState: StateFlow<State<Boolean>> = _loginState.asStateFlow()

    fun loginUser(email: String,password: String) {
        viewModelScope.launch {
            _loginState.value = State.Loading()
            val result = credentialsUseCase(email, password)
            Log.d("VM", "Login result: ${result}")
            _loginState.value = result
        }
    }


    private val _mangaState = MutableStateFlow<State<Manga>>(State.Loading())
    val mangaState: StateFlow<State<Manga>> = _mangaState.asStateFlow()

    fun fetchManga(page: Int) {
        viewModelScope.launch {
            _mangaState.value = State.Loading()

            val result = getAllMangaUseCase(page)

            _mangaState.value = result
        }
    }


    private val _mangaSpecific = MutableStateFlow<State<Data>>(State.Loading())
    val mangaSpecific  = _mangaSpecific.asStateFlow()

    fun fetchMangaId(id: String) {
        viewModelScope.launch {
            _mangaSpecific.value = State.Loading()
            _mangaSpecific.value = getMangaUseCase(id)
        }
    }

}