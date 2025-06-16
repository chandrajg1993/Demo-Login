package com.scci.demoapplogin.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.google.gson.Gson
import com.scci.demoapplogin.response.ValidateTokenReponse
import com.scci.demoapplogin.utility.ApiResponseAny
import com.scci.demoapplogin.utility.Constant.IS_LOGIN
import com.scci.demoapplogin.utility.Constant.LOGIN_DATA
import com.scci.demoapplogin.utility.Constant.USER_DATA
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PrefrenceRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setLogin() {
        val dataStoreKey = preferencesKey<Boolean>(IS_LOGIN)
        dataStore.edit { settings ->
            settings[dataStoreKey] = true
        }
    }

    suspend fun setLogout() {
        val dataStoreKey = preferencesKey<Boolean>(IS_LOGIN)
        dataStore.edit { settings ->
            settings[dataStoreKey] = false
        }
    }

    suspend fun isLogin(): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(IS_LOGIN)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun setUserData(value: ApiResponseAny?) {
        val dataStoreKey = preferencesKey<String>(USER_DATA)
        dataStore.edit { settings ->
            settings[dataStoreKey] = Gson().toJson(value)
        }
    }

    suspend fun getUserData(): ValidateTokenReponse {
        val dataStoreKey = preferencesKey<String>(USER_DATA)
        val preferences = dataStore.data.first()

        //        return  preferences[dataStoreKey]

        val gson = Gson()
        return gson.fromJson(preferences[dataStoreKey].toString(), ValidateTokenReponse::class.java)
    }


    suspend fun getLoginData(): ValidateTokenReponse {
        val dataStoreKey = preferencesKey<String>(LOGIN_DATA)
        val preferences = dataStore.data.first()

        //        return  preferences[dataStoreKey]

        val gson = Gson()
        return gson.fromJson(preferences[dataStoreKey].toString(), ValidateTokenReponse::class.java)
    }

}