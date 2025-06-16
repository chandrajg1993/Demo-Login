package com.scci.demoapplogin.utility

import android.app.Activity
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.scci.demoapplogin.R


object Utill {
    fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION") drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun print(message: String?) {
        println(message)
    }

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


    fun Toast.showCustomToastRed(message: String, activity: Activity) {
        val layout = activity.layoutInflater.inflate(R.layout.custom_tostred_layout, activity.findViewById(R.id.toast_container))

        // set the text of the TextView of the message
        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = message

        // use the application extension function
        this.apply {
            setGravity(Gravity.CENTER, 0, 40)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

}