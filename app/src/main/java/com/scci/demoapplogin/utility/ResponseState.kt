package com.scci.demoapplogin.utility


sealed class ResponseState<T>(val msg: String? = null, val code: Int = 0, val data: T? = null) {

    class Success<T>(msg: String, code: Int, data: T) : ResponseState<T>(msg, code, data)
    class Error<T>(msg: String, code: Int) : ResponseState<T>(msg, code)
}