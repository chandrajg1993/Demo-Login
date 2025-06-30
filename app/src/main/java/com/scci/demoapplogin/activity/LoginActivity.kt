package com.scci.demoapplogin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.scci.demoapplogin.BuildConfig
import com.scci.demoapplogin.databinding.ActivityLoginBinding
import com.scci.demoapplogin.dialogs.CustomMessageDialog
import com.scci.demoapplogin.dialogs.CustomProgressDialog
import com.scci.demoapplogin.repository.HomeViewModal
import com.scci.demoapplogin.response.CanCollectionOTPResponse
import com.scci.demoapplogin.utility.ApiState
import com.scci.demoapplogin.utility.CommonMethod
import com.scci.demoapplogin.utility.Constant
import com.scci.demoapplogin.utility.Utill
import com.scci.demoapplogin.utility.Utill.showCustomToastRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val login_viewmodel: HomeViewModal by viewModels()
    protected lateinit var progressDialog: CustomProgressDialog
    lateinit var messageDialog: CustomMessageDialog

    var versionCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        messageDialog = CustomMessageDialog()
        progressDialog = CustomProgressDialog(this@LoginActivity)

        binding.loginToolbar.toolbarTitle.text = "Demo Login"
        setSupportActionBar(binding.loginToolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        versionCode = BuildConfig.VERSION_CODE

        binding.btnLogin.setOnClickListener({
            if (binding.etMobileno.text.toString().isNullOrEmpty()) {
                binding.etMobileno.setError("Mobile number is required!")
                binding.etMobileno.requestFocus()
            } else if (!CommonMethod.isValidPhone(binding.etMobileno.text.toString().trim { it <= ' ' })) {
                binding.etMobileno.setError("Please enter valid mobile number!")
                binding.etMobileno.requestFocus()
            } else {
                if (Utill.isOnline(this@LoginActivity)) {
                    val forgot = Intent(this@LoginActivity, OTPVerificationActivity::class.java)
                    forgot.putExtra("mobileNumber", binding.etMobileno.text.toString())
                    forgot.putExtra("OTP", "1234")
                    startActivity(forgot)

                    // login_viewmodel.sendCanCollectionLoginOTP(binding.etMobileno.text.toString(), versionCode)
                } else {
                    messageDialog.show(this@LoginActivity, CustomMessageDialog.MessageType.ERROR, Constant.NETWORK_NOTFOUND).setOnDismissListener {
                        messageDialog.dialog.dismiss()
                    }
                }
            }
        })
        login_viewmodel.productList()
        login_viewmodel.productList.observe(this, { state ->
            when (state) {
                is ApiState.Loading -> {
                    if (!progressDialog.isShowing) {
                        progressDialog.show(this, "Please wait...")
                    }
                }

                is ApiState.Failure -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }

                is ApiState.Success -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    Toast.makeText(this, state.body.toString(), Toast.LENGTH_LONG).show()
                }
            }

        })
        /*

                login_viewmodel.loginResponse.observe(this, { state ->
                    when (state) {
                        is ApiState.Loading -> {
                            if (!progressDialog.isShowing) {
                                progressDialog.show(this, "Please wait...")
                            }
                        }
                        is ApiState.Failure -> {
                            if (progressDialog.isShowing) {
                                progressDialog.dialog.dismiss()
                            }
                            messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                        }
                        is ApiState.Success -> {
                            if (progressDialog.isShowing) {
                                progressDialog.dialog.dismiss()
                            }

                            if (state.body.rs == 0) {
                                val resp = Gson().fromJson(Gson().toJson(state.body.data), CanCollectionOTPResponse::class.java)

                                val forgot = Intent(this@LoginActivity, OTPVerificationActivity::class.java)
                                forgot.putExtra("mobileNumber", resp[0].CanCollectionAgentPhoneNo)
                                forgot.putExtra("OTP", resp[0].OTP)
                                startActivity(forgot)
                            } else if(state.body.rs == 1){
                                binding.etMobileno.setText("")
                                Toast(this).showCustomToastRed(state.body.msg, this)
                            } else if(state.body.rs == 2) {
                                binding.etMobileno.setText("")
                                Toast(this).showCustomToastRed(state.body.msg, this)
                            } else {
                                Toast(this).showCustomToastRed(state.body.msg, this)
                            }
                        }
                    }
                })
        */

    }


    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

}