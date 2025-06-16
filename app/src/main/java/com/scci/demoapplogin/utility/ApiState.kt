package com.scci.demoapplogin.utility


sealed class ApiState<T>() {
    class Success<T>(val body: T, val message: String) : ApiState<T>()

    class Failure<T>(val errorText: String, val errorCode: Int) : ApiState<T>()
    class Loading<T>() : ApiState<T>()

}
