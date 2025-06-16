package com.scci.demoapplogin.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import com.scci.demoapplogin.R
import com.scci.demoapplogin.databinding.DialogProgressBinding


class CustomProgressDialog(context: Context) {
    var dialog: CustomDialog
    var isShowing = false

    private lateinit var binding: DialogProgressBinding


    init {
        dialog = CustomDialog(context)

        //        dialog.setCancelable(false)
        dialog.setOnCancelListener {
            isShowing = false;
        }

    }

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun dismis(context: Context, isDialogValue: Boolean): Dialog {
        isShowing = isDialogValue
        dialog.dismiss()
        return dialog
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val inflater = (context as Activity).layoutInflater
        binding = DialogProgressBinding.inflate(inflater)

        if (title != null) {
            binding.cpTitle.text = title
        }

        // Card Color
        binding.cpCardview.setCardBackgroundColor(Color.parseColor("#70000000"))

        // Progress Bar Color
        //        setColorFilter(view.cp_pbar.indeterminateDrawable, ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null))

        // Text Color
        binding.cpTitle.setTextColor(Color.WHITE)

        if (dialog.isShowing()) {
            dialog.cancel()
        }
        dialog.setContentView(binding.root)
        if (context != null && !context.isFinishing()) {
            dialog.show()
        }


        isShowing = true
        return dialog
    }

    fun dismisDailogBox() {
        dialog.dismiss()
        isShowing = false

    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION") drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

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

}