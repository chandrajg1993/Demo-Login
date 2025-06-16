package com.scci.demoapplogin.request

data class ValidateOTPReq(
    val PhoneNo: String = "",
    val OTP: String = "",
    val AppVersion: Int = 0)