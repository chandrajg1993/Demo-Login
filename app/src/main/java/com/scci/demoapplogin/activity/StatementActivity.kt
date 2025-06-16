package com.scci.demoapplogin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.scci.demoapplogin.databinding.ActivityStatementBinding
import com.scci.demoapplogin.dialogs.CustomMessageDialog
import com.scci.demoapplogin.dialogs.CustomProgressDialog
import com.scci.demoapplogin.repository.HomeViewModal
import com.scci.demoapplogin.utility.CommonMethod
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatementActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatementBinding
    lateinit var commonMethod: CommonMethod
    private val login_viewmodel: HomeViewModal by viewModels()
    protected lateinit var progressDialog: CustomProgressDialog
    lateinit var messageDialog: CustomMessageDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commonMethod = CommonMethod(this@StatementActivity)
        messageDialog = CustomMessageDialog()
        progressDialog = CustomProgressDialog(this@StatementActivity)

        setSupportActionBar(binding.toolbarStatement.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbarStatement.toolbarTitle.setText("Statement")


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@StatementActivity,DashBoardActivity::class.java))
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@StatementActivity,DashBoardActivity::class.java))
    }

}