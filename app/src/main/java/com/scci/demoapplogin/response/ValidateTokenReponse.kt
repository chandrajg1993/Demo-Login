package com.scci.demoapplogin.response

class ValidateTokenReponse : ArrayList<ValidateTokenModel>()

data class ValidateTokenModel(
    val CanCollectionAgentID: Int = 0,
    val CanCollectionAgentPhoneNo: String = "",
    val CanCollectionAgentName: String = "",
    val CanCollectionAgentSurName: String = "",
    val SalesPersonCode: String = "",
    val IsActive: Boolean,
    val OTP: String = "",
    val GUID: String = "",
    val GeneratedOn: String = ""
)


