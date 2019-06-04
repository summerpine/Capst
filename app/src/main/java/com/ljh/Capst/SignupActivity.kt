package com.ljh.Capst

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        tblrow_private.setOnClickListener{
            ToSignupPrivate()
        }
        tblrow_youtuber.setOnClickListener {
            ToSignupYoutuber()
        }
        tblrow_company.setOnClickListener {
            ToSignupCompany()
        }

        link_login.setOnClickListener {
            ToLogin()
        }
    }
    fun ToSignupPrivate(){
        startActivity(Intent(this, SignupPrivateActivity::class.java))
//        val nextIntent = Intent(this, SignupPrivateActivity::class.java)
//        startActivity(nextIntent)
        finish()
    }
    fun ToSignupYoutuber(){
        startActivity(Intent(this,SignupYoutuberActivity::class.java))
        finish()
    }
    fun ToSignupCompany(){
        startActivity(Intent(this,SignupCompanyActivity::class.java))
        finish()
    }
    fun ToLogin(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}