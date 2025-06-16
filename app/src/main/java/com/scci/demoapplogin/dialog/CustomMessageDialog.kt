package com.scci.demoapplogin.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.scci.demoapplogin.R
import com.scci.demoapplogin.databinding.DialogMessageBinding
import com.scci.demoapplogin.utility.Utill

class CustomMessageDialog {

    enum class MessageType {
        SUCCESS, ERROR, WARNING, NOTHING
    }

    lateinit var dialog: CustomDialog
    var isShowing = false

    private lateinit var binding: DialogMessageBinding

    fun show(context: Context): Dialog {
        return show(context, MessageType.NOTHING, "")
    }

    @SuppressLint("ResourceAsColor")
    fun show(context: Context, messageType: MessageType, message: String): Dialog {
        val inflater = (context as Activity).layoutInflater
        binding = DialogMessageBinding.inflate(inflater)

        //        val view = inflater.inflate(R.layout.dialog_message, null)

        when (messageType) {

            MessageType.SUCCESS -> {
                Utill.setColorFilter(binding.msgDialogHeader.background,
                    ResourcesCompat.getColor(context.resources,
                        R.color.colorWhite,
                        null)) //                view.msgDialogHeader.setBackgroundColor(R.color.green)
                binding.msgDialogImg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                binding.msgDialogText.text = message
                binding.msgDialogTitle.text = "Success"
            }
            MessageType.ERROR -> {
                Utill.setColorFilter(binding.msgDialogHeader.background,
                    ResourcesCompat.getColor(context.resources,
                        R.color.colorWhite,
                        null)) //                view.msgDialogHeader.setBackgroundColor(R.color.redTxt)
                binding.msgDialogImg.setImageResource(R.drawable.ic_baseline_error_outline_24)
                binding.msgDialogText.text = message
                binding.msgDialogTitle.text = "Error"
            }
            MessageType.WARNING -> {
                Utill.setColorFilter(binding.msgDialogHeader.background,
                    ResourcesCompat.getColor(context.resources,
                        R.color.colorWhite,
                        null)) //                view.msgDialogHeader.setBackgroundColor(R.color.teal_500)
                binding.msgDialogImg.setImageResource(R.drawable.ic_baseline_error_outline_24)
                binding.msgDialogText.text = message
                binding.msgDialogTitle.text = "Warning"
            }
            MessageType.NOTHING -> {
                Utill.setColorFilter(binding.msgDialogHeader.background, ResourcesCompat.getColor(context.resources, R.color.colorWhite, null))
                binding.msgDialogImg.visibility = View.GONE
                binding.msgDialogText.text = ""
                binding.msgDialogTitle.text = ""
            }

        }

        binding.msgDialogButton.setOnClickListener { //hideDeilog()
            dialog.dismiss()
            isShowing = false;
        }

        //        try {
        //            if(dialog != null){
        //                dialog.dismiss()
        //                isShowing = false;
        //            }
        //        } catch (e : Exception) {
        //            e.toString()
        //        }
        try {
            if (dialog == null) {
                dialog = CustomDialog(context)
            }
        } catch (e: Exception) {
            dialog = CustomDialog(context)
        }/*else{
            dialog = CustomDialog(context)
        }*/

        //        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //        dialog.setCancelable(false)
        dialog.setContentView(binding.root) //        dialog.setCancelable(false)

        dialog.setOnCancelListener {
            dialog.dismiss();
            isShowing = false;
        }
        try {/*if (dialog.isShowing()) {
                dialog.dismiss();
                isShowing = false;
            }*/
            if ((!dialog.isShowing()) && (!isShowing)) {
                dialog.show()
            }
        } catch (e: Exception) {
        }

        isShowing = true;
        return dialog
    }

    fun hideDeilog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    isShowing = false;
                    dialog.dismiss()
                }
            }
        } catch (e: Exception) {
        }

    }

    //    private fun setColorFilter(drawable: Drawable, color: Int) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    //        } else {
    //            @Suppress("DEPRECATION")
    //            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    //        }
    //    }

    class CustomDialog(context: Context) : Dialog(context, R.style.ProgressDialogTheme) {
        init { // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.semi_transparent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                    insets.consumeSystemWindowInsets()
                }
            }
        }
    }

    /*var hideCustomDialog(){

    }*/

}