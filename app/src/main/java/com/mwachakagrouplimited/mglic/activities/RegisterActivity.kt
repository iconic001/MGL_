package com.mwachakagrouplimited.mglic.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.widget.Toolbar

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.firestore.FirestoreClass
import com.mwachakagrouplimited.mglic.model.User


class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        setupActionBar()


        val login = findViewById<TextView>(R.id.tv_login)

        login.setOnClickListener{
            onBackPressed()
        }

        val register = findViewById<Button>(R.id.btn_register)

        register.setOnClickListener {
            registerUser()
        }
    }

    private fun setupActionBar(){

        val toolbar = findViewById<Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(toolbar)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }



    private fun validateRegisterDetails(): Boolean{
        val firstName = findViewById<EditText>(R.id.et_first_name)
        val lastName = findViewById<EditText>(R.id.et_last_name)
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val confirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val termsAndCondition = findViewById<CheckBox>(R.id.cb_terms_and_conditions)
        return when {
            TextUtils.isEmpty (firstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(lastName.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(email.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(password.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(confirmPassword.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            password.text.toString().trim { it <= ' ' } != confirmPassword.text.toString()
                .trim { it <= ' '} -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false

                }
            !termsAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                //showErrorSnackBar("Thank you for registering", false)
                true
            }
        }
    }

    private fun registerUser() {

        if(validateRegisterDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val firstName = findViewById<EditText>(R.id.et_first_name)
            val lastName = findViewById<EditText>(R.id.et_last_name)
            val email = findViewById<EditText>(R.id.et_email)
            val password = findViewById<EditText>(R.id.et_password)

            val Email: String = email.text.toString().trim { it <= ' '}
            val Password: String = password.text.toString().trim { it <= ' '}


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(
                    OnCompleteListener <AuthResult> {task ->

                        if(task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,

                                firstName.text.toString().trim { it <= ' '},
                                lastName.text.toString().trim { it <= ' '},
                                email.text.toString().trim { it <= ' '}

                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            /*showErrorSnackBar(
                                "You have registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )*/

                            //FirebaseAuth.getInstance().signOut()
                            //finish()

                        }else{
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )

        }
    }

    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,resources.getString(R.string.register_success),
            Toast.LENGTH_LONG

        ).show()
    }

}