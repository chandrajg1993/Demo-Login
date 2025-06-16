package com.scci.demoapplogin.utility

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.scci.demoapplogin.R
import java.util.regex.Pattern

class CommonMethod(var mContext: Context) {
    lateinit var sharedPreferenceseded: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var mActivity: Activity? = null

    fun setStringData(key: String?, value: String?) {
        sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(mContext)
        editor = sharedPreferenceseded.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getStringData(key: String?): String? {
        sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(mContext)
        return sharedPreferenceseded.getString(key, "")
    }

    fun setIsActive(key: String?, value: Boolean?): Boolean {
        sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(mContext)
        editor = sharedPreferenceseded.edit()
        editor.putBoolean(key, value!!)
        editor.commit()
        return true
    }

    fun getIsActive(key: String?): Boolean {
        sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(mContext)
        return sharedPreferenceseded.getBoolean(key, false)
    }

    companion object {
        const val PHONE_PATTERN = "^[+09876]\\d{9}$"

        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo: NetworkInfo?
            try {
                netInfo = cm.activeNetworkInfo
                if (netInfo != null && netInfo.isConnectedOrConnecting) {
                    return true
                }
            } catch (e: Exception) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return false
        }

        fun closeKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun setTokenKey(context: Context?, status: String?) {
            val sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferenceseded.edit()
            editor.putString("TOKEN", status)
            editor.commit()
        }

        fun getTokenKey(context: Context?): String? {
            val sharedPreferenceseded = PreferenceManager.getDefaultSharedPreferences(context) //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sharedPreferenceseded.getString("TOKEN", "")
        }

        fun displaySnackbarerror(view: View?, context: Context, s: String?) {
            try {
                val snack = Snackbar.make(view!!, s!!, Snackbar.LENGTH_LONG)
                val sbview = snack.view
                sbview.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                val textView = sbview.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(context.resources.getColor(R.color.colorWhite))
                snack.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun displaySnackbarLogin(view: View?, context: Context, s: String?) {
            try {
                val snack = Snackbar.make(view!!, s!!, Snackbar.LENGTH_LONG)
                val sbview = snack.view
                sbview.setBackgroundColor(ContextCompat.getColor(context, R.color.darkModeColor_yellow))
                val textView = sbview.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(context.resources.getColor(R.color.colorWhite))
                snack.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isValidPhone(phone: String?): Boolean {
            val pattern = Pattern.compile(PHONE_PATTERN)
            val matcher = pattern.matcher(phone)
            return matcher.matches()
        }

    }


    init {
        try {
            mActivity = mContext as Activity
        } catch (ee: Exception) {
        }
    }
}