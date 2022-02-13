package com.mwachakagrouplimited.mglic.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.firestore.FirestoreClass
import com.mwachakagrouplimited.mglic.model.User
import com.mwachakagrouplimited.mglic.utils.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    //FOR FULL SCREEN

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }*/

        val register = findViewById<TextView>(R.id.tv_register)
        val forgotPassword = findViewById<TextView>(R.id.tv_forgotPassword)
        val login = findViewById<Button>(R.id.btn_login)

        register.setOnClickListener(this)
        forgotPassword.setOnClickListener(this)
        login.setOnClickListener(this)

    }

    fun userLoggedInSuccess (user: User) {
        hideProgressDialog()

        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)

        } else { startActivity(Intent(this@LoginActivity, MainActivity::class.java))

        }
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.tv_forgotPassword -> {

                val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_login -> {
                logInRegisteredUser()
                validateLoginDetails()
            }
            R.id.tv_register -> {

                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateLoginDetails(): Boolean {

        val email = findViewById<EditText>(R.id.et_email_login)
        val password = findViewById<EditText>(R.id.til_password)


        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details have been confirmed", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email = findViewById<EditText>(R.id.et_email_login)
            val password = findViewById<EditText>(R.id.til_password)

            val Email: String = email.text.toString().trim { it <= ' ' }
            val Password: String = password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        FirestoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                        Toast.makeText( this,
                            "Please make sure you have registered first", Toast.LENGTH_LONG).show()
                    }
                }
        }

        }
}