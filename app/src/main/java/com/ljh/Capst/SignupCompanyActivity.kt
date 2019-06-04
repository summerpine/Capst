package com.ljh.Capst

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ljh.Capst.model.UserInfoPrivateDTO
import kotlinx.android.synthetic.main.activity_signup_private.*

class SignupCompanyActivity: AppCompatActivity(){
    var storage : FirebaseStorage? = null
    var auth : FirebaseAuth?= null
    var firestore : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_company)
        btn_signup.setOnClickListener {
            Submit()
        }
        link_login.setOnClickListener {
            ToLogin()
        }
    }
    fun Submit() {
        val _input_email: EditText = findViewById(R.id.input_email)
        val _input_password: EditText = findViewById(R.id.input_password)
        val _input_reEnterPassword: EditText = findViewById(R.id.input_reEnterPassword)
        val _input_mobile: EditText = findViewById(R.id.input_mobile)
        val _input_name: EditText = findViewById(R.id.input_name)

        var email = _input_email.text.toString()
        var password = _input_password.text.toString()
        var reEnterPassword = _input_reEnterPassword.text.toString()
        var name = _input_name.text.toString()
        var moblie = _input_mobile.text.toString()

        if (!email.isEmpty() && !password.isEmpty() && !reEnterPassword.isEmpty()
            && !name.isEmpty() && !moblie.isEmpty()) {
            if (password != reEnterPassword) {
                Toast.makeText(this, "비밀번호와 비밀번호 재입력에 입력된 값은 같아야합니다.", Toast.LENGTH_LONG).show()
            }
            // 비밀번호 = 비밀번호 재입력
            else {
                storage = FirebaseStorage.getInstance()
                auth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
                var UserDTO = UserInfoPrivateDTO()

                auth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            UserDTO.uid = auth?.currentUser?.uid // uid
                            UserDTO.userEmail = auth?.currentUser?.email // email
                            UserDTO.userName = name // 이름
                            UserDTO.userMobile = moblie// 모바일

                            firestore?.collection("UserInfoCompany")?.document()?.set(UserDTO)
                            setResult(Activity.RESULT_OK)
                            Toast.makeText(this, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_LONG).show()
                            ToLogin()
                        }
                        // task -> isFailed
                        else {
                            Toast.makeText(
                                this@SignupCompanyActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } // task -> isFaield
                    } // addOnCompleteListener(this)
            } // else: 비밀번호 = 비밀번호 재입력
        } // 공란 없음
        else{
            Toast.makeText(this, "입력되지 않은 칸을 채워주십시오.", Toast.LENGTH_LONG).show()
        }// if: empty검사
    } // Submit()
    fun ToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}