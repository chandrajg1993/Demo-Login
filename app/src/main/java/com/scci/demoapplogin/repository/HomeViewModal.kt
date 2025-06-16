package com.scci.demoapplogin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scci.demoapplogin.request.*
import com.scci.demoapplogin.utility.ResponseState
import com.scci.demoapplogin.utility.ApiResponseAny
import com.scci.demoapplogin.utility.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModal @Inject constructor(private val repository: HomeRepository, private val prefrenceRepository: PrefrenceRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<ApiState<ApiResponseAny>>()
    val loginResponse: LiveData<ApiState<ApiResponseAny>>
        get() = _loginResponse


    private val _validateOTPResponse = MutableLiveData<ApiState<ApiResponseAny>>()
    val validateOTPResponse: LiveData<ApiState<ApiResponseAny>>
        get() = _validateOTPResponse


    private var isLogin = false


    fun sendCanCollectionLoginOTP(mobileNo: String, appVersion: Int) {
        viewModelScope.launch {

            _loginResponse.value = ApiState.Loading()
            val sendCanCollectionLoginData = SendCanCollectionLoginReq(mobileNo, appVersion)

            when (val loginResponse = repository.SendCanCollectionLoginOTP(sendCanCollectionLoginData)) {

                is ResponseState.Error -> _loginResponse.value = ApiState.Failure(loginResponse.msg!!, errorCode = 0)

                is ResponseState.Success -> {
                    try {
                        _loginResponse.value = ApiState.Success(loginResponse.data!!, loginResponse.msg!!)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

            }

        }
    }


    fun validateOTP(validateOTPReq: ValidateOTPReq) {
        viewModelScope.launch {

            _validateOTPResponse.value = ApiState.Loading()

            when (val validateOTPResponse = repository.validateOTP(validateOTPReq)) {

                is ResponseState.Error -> _validateOTPResponse.value = ApiState.Failure(validateOTPResponse.msg!!, errorCode = 0)

                is ResponseState.Success -> {
                    try {
                        prefrenceRepository.setUserData(validateOTPResponse.data)
                        prefrenceRepository.setLogin()
                        isLogin = true
                        _validateOTPResponse.value = ApiState.Success(validateOTPResponse.data!!, validateOTPResponse.msg!!)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

            }

        }
    }

}