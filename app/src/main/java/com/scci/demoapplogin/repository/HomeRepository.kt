package com.scci.demoapplogin.repository

import android.accounts.NetworkErrorException
import com.scci.demoapplogin.request.*
import com.scci.demoapplogin.utility.Constant
import com.scci.demoapplogin.utility.Constant.EXCEPTION_ERROR
import com.scci.demoapplogin.utility.ResponseState
import com.scci.demoapplogin.utility.Utill
import com.scci.demoapplogin.di.GcPosApis
import com.scci.demoapplogin.response.ProductList
import com.scci.demoapplogin.utility.ApiResponseAny
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apis: GcPosApis) {

    suspend fun SendCanCollectionLoginOTP(sendCanCollectionLoginReq: SendCanCollectionLoginReq): ResponseState<ApiResponseAny?> {
        return try {
            Utill.print("isSuccessful = 0")
            val response = apis.sendCanCollectionLoginOTP(sendCanCollectionLoginReq)

            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else if (response.body()!!.rs == 1) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else if (response.body()!!.rs == 2) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else {
                    ResponseState.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                ResponseState.Error("${response.message()}", response.code())
            }
        } catch (e: UnknownHostException) {
            ResponseState.Error(Constant.HOST_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: ConnectException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: NetworkErrorException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: Exception) {
            ResponseState.Error(e.message!!, EXCEPTION_ERROR)
        }
    }


    suspend fun validateOTP(validateOTPReq: ValidateOTPReq): ResponseState<ApiResponseAny?> {
        return try {
            Utill.print("isSuccessful = 0")
            val response = apis.validateCanCollectionOTP(validateOTPReq)

            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else if (response.body()!!.rs == 1) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else if (response.body()!!.rs == 2) {
                    ResponseState.Success(response.message(), response.body()!!.rs, response.body())
                } else {
                    ResponseState.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                ResponseState.Error("${response.message()}", response.code())
            }
        } catch (e: UnknownHostException) {
            ResponseState.Error(Constant.HOST_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: ConnectException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: NetworkErrorException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: Exception) {
            ResponseState.Error(e.message!!, EXCEPTION_ERROR)
        }
    }

    suspend fun productList(): ResponseState<ProductList?> {
        return try {
            Utill.print("isSuccessful = 0")
            val response = apis.products()


            if (response.code() == 200) {
                ResponseState.Success(response.message(), response.code(), response.body())
            } else {
                ResponseState.Error("${response.message()}", response.code())
            }

        } catch (e: UnknownHostException) {
            ResponseState.Error(Constant.HOST_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: ConnectException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: NetworkErrorException) {
            ResponseState.Error(Constant.NETWORK_NOTFOUND!!, EXCEPTION_ERROR)
        } catch (e: Exception) {
            ResponseState.Error(e.message!!, EXCEPTION_ERROR)
        }
    }


}