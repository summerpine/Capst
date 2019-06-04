package com.ljh.Capst

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.facebook.login.Login
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.ljh.Capst.model.ContentDTO
import com.ljh.Capst.model.UserInfoPrivateDTO
import kotlinx.android.synthetic.main.activity_signup_private.*

class SignupPrivateActivity: AppCompatActivity() {
    //    var auth : FirebaseAuth = FirebaseAuth.getInstance()
//    lateinit var mAuth: FirebaseAuth
//    lateinit var mDatabaseReferece: DatabaseReference
//    lateinit var mDatabase: FirebaseDatabase
    var storage : FirebaseStorage? = null
    var auth : FirebaseAuth?= null
    var firestore : FirebaseFirestore? = null

    private val TAG = "SignupPrivateActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_private)
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

        var email_private = _input_email.text.toString()
        var password_private = _input_password.text.toString()
        var reEnterPassword_private = _input_reEnterPassword.text.toString()
        var name_private = _input_name.text.toString()
        var moblie_private = _input_mobile.text.toString()



//        private var mProgressBar: ProgressDialog? = null


        if (!email_private.isEmpty() && !password_private.isEmpty() && !reEnterPassword_private.isEmpty()
            && !name_private.isEmpty() && !moblie_private.isEmpty()) {
            if (password_private != reEnterPassword_private) {
                Toast.makeText(this, "비밀번호와 비밀번호 재입력에 입력된 값은 같아야합니다.", Toast.LENGTH_LONG).show()
            }
            // 비밀번호 = 비밀번호 재입력
            else {
                storage = FirebaseStorage.getInstance()
                auth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
                var UserDTO = UserInfoPrivateDTO()

//                mDatabase = FirebaseDatabase.getInstance()
//                mDatabaseReferece = mDatabase!!.reference!!.child("Users")
//                mAuth = FirebaseAuth.getInstance()

                auth!!
                    .createUserWithEmailAndPassword(email_private!!, password_private!!)
                    .addOnCompleteListener(this) { task ->
                        //                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success")
//                            val userId = auth!!.currentUser!!.uid
                            UserDTO.uid = auth?.currentUser?.uid // uid
                            UserDTO.userEmail = auth?.currentUser?.email // email
                            UserDTO.userName = name_private // 이름
                            UserDTO.userMobile = moblie_private// 모바일

                            firestore?.collection("UserInfoPrivate")?.document()?.set(UserDTO)
                            setResult(Activity.RESULT_OK)
                            Toast.makeText(this, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_LONG).show()
                            ToLogin()



                            //Verify Email
//                            verifyEmail();
                            //update user profile information
//                            val currentUserDb = mDatabaseReferece!!.child(userId)
//                            currentUserDb.child("userName").setValue(name_private)
//                            currentUserDb.child("userMobile").setValue(moblie_private)
//                            updateUserInfoAndUI()
                        }
                        // task -> isFailed
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this@SignupPrivateActivity, "Authentication failed.",
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

//    fun verifyEmail() {
//        val mUser = auth!!.currentUser;
//        mUser!!.sendEmailVerification()
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(
//                        this@SignupPrivateActivity,
//                        "Verification email sent to " + mUser.getEmail(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Log.e(TAG, "sendEmailVerification", task.exception)
//                    Toast.makeText(
//                        this@SignupPrivateActivity,
//                        "Failed to send verification email.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }

//    fun updateUserInfoAndUI() {
//        //start next activity
//        val intent = Intent(this@SignupPrivateActivity, LoginActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
//    }

    fun ToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}


//class User(val uid:String, val userName: String, val userMobile:String)
//data class User(
//    var uid : String,
////    var uid: String = "",
//    var userName: String = "",
//    var userMobile: String =""
//)



//                auth.createUserWithEmailAndPassword(email_private, password_private).addOnCompleteListener(this, OnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val currentUser = auth.currentUser
//                        val uid = currentUser!!.uid
//                        mDatabase = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//                        val user = User(currentUser.toString(), name_private, moblie_private)
//                        mDatabase.setValue(user)
////                        mDatabase.child("users").child("uid").setValue(uid)
////                        mDatabase.child("users").child("userName").setValue(name_private)
////                        mDatabase.child("users").child("userMobile").setValue(moblie_private)
//
//
////                        mDatabase.database.getReference("/users/$uid")
////                        user.updateProfile()
//
////                        mDatabase.child("users").child(uid).child("Name").setValue(name_private)
////                        mDatabase.child("users").child(uid).child("Mobile").setValue(moblie_private)
////                        mDatabase.child(uid).child("Name").setValue(name_private)
////                        mDatabase.child(uid).child("Mobile").setValue(moblie_private)
//                        Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
//
////                        startActivity(Intent(this, LoginActivity::class.java))
////                        finish()
//                    }else {
//                        Toast.makeText(this, "오류: 다시 시도하여 주십시오.", Toast.LENGTH_LONG).show()
//                    }
//                })
//                else {
//                    Toast.makeText(this, "입력되지 않은 칸을 채워주십시오.", Toast.LENGTH_LONG).show()
//                }
//            }

// 공란
