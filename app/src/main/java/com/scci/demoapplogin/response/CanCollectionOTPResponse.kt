package com.scci.demoapplogin.response

class CanCollectionOTPResponse : ArrayList<CanCollectionOTPModel>()

data class CanCollectionOTPModel (
    val CanCollectionAgentPhoneNo: String = "",
    val OTP: String = ""
)