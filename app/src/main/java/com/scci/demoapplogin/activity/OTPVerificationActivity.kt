package com.scci.demoapplogin.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.scci.demoapplogin.BuildConfig
import com.scci.demoapplogin.R
import com.scci.demoapplogin.countdowntimer.CircularView
import com.scci.demoapplogin.countdowntimer.CircularViewCallback
import com.scci.demoapplogin.databinding.ActivityOtpverificationBinding
import com.scci.demoapplogin.dialogs.CustomMessageDialog
import com.scci.demoapplogin.dialogs.CustomProgressDialog
import com.scci.demoapplogin.repository.HomeViewModal
import com.scci.demoapplogin.request.ValidateOTPReq
import com.scci.demoapplogin.response.CanCollectionOTPResponse
import com.scci.demoapplogin.response.ValidateTokenReponse
import com.scci.demoapplogin.utility.ApiState
import com.scci.demoapplogin.utility.CommonMethod
import com.scci.demoapplogin.utility.Constant
import com.scci.demoapplogin.utility.Utill
import com.scci.demoapplogin.utility.Utill.showCustomToastRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPVerificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpverificationBinding
    private val login_viewmodel: HomeViewModal by viewModels()
    var versionCode: Int = 0
    var OTP: String = ""

    protected lateinit var progressDialog: CustomProgressDialog
    lateinit var messageDialog: CustomMessageDialog
    lateinit var commonMethod: CommonMethod

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commonMethod = CommonMethod(this@OTPVerificationActivity)
        messageDialog = CustomMessageDialog()
        progressDialog = CustomProgressDialog(this@OTPVerificationActivity)

        setSupportActionBar(binding.toolbarOTP.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val upArrow = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24)
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbarOTP.toolbarTitle.setText("Validate OTP")

        versionCode = BuildConfig.VERSION_CODE

        binding.tvMobileforverify.setText("Sent to " + intent.getStringExtra("mobileNumber"))
        OTP = intent.getStringExtra("OTP")!!
        time_start()

        binding.btnVerification.setOnClickListener({
            if (binding.otpEdittext.text!!.length >= 4) {
                if (OTP == binding.otpEdittext.text.toString()) {
                    val validateOTPReq = ValidateOTPReq(
                        intent.getStringExtra("mobileNumber").toString(),
                        binding.otpEdittext.text.toString(), versionCode
                    )

                    if (Utill.isOnline(this@OTPVerificationActivity)) {
                       // login_viewmodel.validateOTP(validateOTPReq)

                        val intentUser = Intent(this@OTPVerificationActivity, DashBoardActivity::class.java)
                        startActivity(intentUser)

                    } else {
                        messageDialog.show(this@OTPVerificationActivity, CustomMessageDialog.MessageType.ERROR, Constant.NETWORK_NOTFOUND).setOnDismissListener {
                            messageDialog.dialog.dismiss()
                        }
                    }
                } else {
                    binding.otpEdittext.setError("Please enter correct OTP.")
                    binding.otpEdittext.requestFocus()
                }
            } else {
                binding.otpEdittext.setError("enter valid OTP!")
                binding.otpEdittext.requestFocus()
            }
        })

        login_viewmodel.validateOTPResponse.observe(this, { state ->
            when (state) {
                is ApiState.Loading -> {
                    if (!progressDialog.isShowing) {
                        progressDialog.show(this, "Please Wait...")
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
                    try {
                        if (state.body.rs == 0) {
                            val resp = Gson().fromJson(Gson().toJson(state.body.data), ValidateTokenReponse::class.java)

                            commonMethod.setIsActive("isLogin", true)
                            commonMethod.setStringData("CanCollectionAgentID", resp[0].CanCollectionAgentID.toString())
                            commonMethod.setStringData("CanCollectionAgentPhoneNo", resp[0].CanCollectionAgentPhoneNo)
                            commonMethod.setStringData("CanCollectionAgentName", resp[0].CanCollectionAgentName)
                            commonMethod.setStringData("CanCollectionAgentSurName", resp[0].CanCollectionAgentSurName)
                            commonMethod.setStringData("SalesPersonCode", resp[0].SalesPersonCode)
                            commonMethod.setIsActive("IsActive", resp[0].IsActive)
                            commonMethod.setStringData("GUID",resp[0].GUID)

                            val intentUser = Intent(this@OTPVerificationActivity, DashBoardActivity::class.java)
                            startActivity(intentUser)
                        } else if (state.body.rs == 1) {
                            Toast(this).showCustomToastRed(state.body.msg, this)
                        } else if (state.body.rs == 2) {
                            Toast(this).showCustomToastRed(state.body.msg, this)
                        } else {
                            Toast(this).showCustomToastRed(state.body.msg, this)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })


        binding.tvResend.setOnClickListener({
            if (CommonMethod.isOnline(this@OTPVerificationActivity)) {
               // login_viewmodel.sendCanCollectionLoginOTP(intent.getStringExtra("mobileNumber").toString(), versionCode)

                Toast(this).showCustomToastRed("OTP sent on registered mobile number.", this@OTPVerificationActivity)
                time_start()
            } else {
                CommonMethod.displaySnackbarLogin(binding.liLayout, this@OTPVerificationActivity, "Unable to connect with network.")
            }
        })

        login_viewmodel.loginResponse.observe(this, { state ->
            when (state) {
                is ApiState.Loading -> {
                    if (!progressDialog.isShowing) {
                        progressDialog.show(this, "Please Wait...")
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

                        OTP = resp[0].OTP
                        Toast(this).showCustomToastRed("OTP sent on registered mobile number.", this@OTPVerificationActivity)
                        time_start()
                    } else if (state.body.rs == 1) {
                        Toast(this).showCustomToastRed(state.body.msg, this)
                    } else if (state.body.rs == 2) {
                        Toast(this).showCustomToastRed(state.body.msg, this)
                    } else {
                        Toast(this).showCustomToastRed(state.body.msg, this)
                    }
                }
            }
        })
    }

    fun time_start() {
        binding.circularView.visibility = View.VISIBLE
        binding.tvResend.visibility = View.GONE
        val builderWithTimer = CircularView.OptionsBuilder().shouldDisplayText(true).setCounterInSeconds(45).setCircularViewCallback(object : CircularViewCallback {
            override fun onTimerFinish() {
                binding.circularView.visibility = View.GONE
                binding.tvResend.visibility = View.VISIBLE
            }

            override fun onTimerCancelled() {}
        })
        binding.circularView.setOptions(builderWithTimer)
        binding.circularView.startTimer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

}