package com.scci.demoapplogin.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.scci.demoapplogin.BuildConfig
import com.scci.demoapplogin.R
import com.scci.demoapplogin.databinding.ActivityDashBoardBinding
import com.scci.demoapplogin.dialogs.CustomMessageDialog
import com.scci.demoapplogin.dialogs.CustomProgressDialog
import com.scci.demoapplogin.iosdialog.iOSDialogBuilder
import com.scci.demoapplogin.repository.HomeViewModal
import com.scci.demoapplogin.utility.CommonMethod
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding
    private var doubleBackToExitPressedOnce = false
    lateinit var commonMethod: CommonMethod
    private val login_viewmodel: HomeViewModal by viewModels()
    protected lateinit var progressDialog: CustomProgressDialog
    lateinit var messageDialog: CustomMessageDialog

    var versionCode: Int? = 0
    var versionName: String? = null
    var AgentFirstName: String? = null
    var AgentLastName: String? = null
    var CanCollectionAgentPhoneNo: String? = null

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commonMethod = CommonMethod(this@DashBoardActivity)
        messageDialog = CustomMessageDialog()
        progressDialog = CustomProgressDialog(this@DashBoardActivity)

        binding.dashboardToolbar.toolbarTitle.text = "Dashboard"
        setSupportActionBar(binding.dashboardToolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        AgentFirstName = commonMethod.getStringData("CanCollectionAgentName")
        AgentLastName = commonMethod.getStringData("CanCollectionAgentSurName")
        CanCollectionAgentPhoneNo = commonMethod.getStringData("CanCollectionAgentPhoneNo")!!

        versionCode = BuildConfig.VERSION_CODE
        versionName = BuildConfig.VERSION_NAME

        try {
            binding.drawerLayoutLayout.tvversioncode.setText("V ${versionName}")
           // binding.drawerLayoutLayout.tvfname.setText("Hello ${AgentFirstName + " " + AgentLastName}")
            binding.drawerLayoutLayout.tvfname.setText("Hello ${"XYZ" + " " + "Singh"}")
           // binding.drawerLayoutLayout.tvMobileNo.setText("${CanCollectionAgentPhoneNo}")
            binding.drawerLayoutLayout.tvMobileNo.setText("${"1234567890"}")
            Picasso.get().load(R.drawable.icon_profile).placeholder(R.drawable.icon_profile).into(binding.drawerLayoutLayout.ivprofile)
        } catch (Ex: Exception) {
            Ex.printStackTrace()
        }

        binding.dashboardToolbar.llMenuData.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayoutLayout.llDashboard.setOnClickListener {
            closeDrawerMethod()
        }

        binding.drawerLayoutLayout.llStatement.setOnClickListener {
            startActivity(Intent(this@DashBoardActivity, StatementActivity::class.java))
            closeDrawerMethod()
        }

        binding.drawerLayoutLayout.llLogout.setOnClickListener {
            iOSDialogBuilder(this@DashBoardActivity).setTitle(resources.getString(R.string.logout)).setSubtitle(resources.getString(R.string.areuslog)).setBoldPositiveLabel(true).setCancelable(false)
                .setPositiveListener(resources.getString(R.string.logoutbtn)) { dialog ->
                    closeDrawerMethod()
                    dialog.dismiss()
                   // commonMethod.setIsActive("isLogin", false)
                    startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
                }.setNegativeListener(resources.getString(R.string.cancelbtn)) { dialog ->
                    dialog.dismiss() }.build().show()
        }

    }

    private fun closeDrawerMethod() {
        timer = object : CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished/1000%60

                if (seconds%2 == 0L) {
                    timer?.cancel()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            override fun onFinish() {
            }
        }
        timer?.start()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        CommonMethod.displaySnackbarLogin(binding.linearLayout, this@DashBoardActivity, "Please click BACK again to exit.")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}