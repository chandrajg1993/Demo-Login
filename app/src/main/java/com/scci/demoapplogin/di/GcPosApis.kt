package com.scci.demoapplogin.di

import com.scci.demoapplogin.request.*
import com.scci.demoapplogin.utility.ApiResponseAny
import com.scci.demoapplogin.utility.Constant.CANCOLLECTIONPOINTSERVICES
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GcPosApis {
    @POST(value = CANCOLLECTIONPOINTSERVICES+"/api/Auth/SendOTPToCollectionAgent")
    suspend fun sendCanCollectionLoginOTP(@Body requestData: SendCanCollectionLoginReq): Response<ApiResponseAny>

    @POST(value = CANCOLLECTIONPOINTSERVICES+"/api/Auth/ValidateCanCollectionAgent")
    suspend fun validateCanCollectionOTP(@Body requestData: ValidateOTPReq): Response<ApiResponseAny>

}