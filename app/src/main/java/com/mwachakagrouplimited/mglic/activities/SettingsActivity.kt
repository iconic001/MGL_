package com.mwachakagrouplimited.mglic.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.firestore.FirestoreClass
import com.mwachakagrouplimited.mglic.model.User
import com.mwachakagrouplimited.mglic.utils.Constants
import com.mwachakagrouplimited.mglic.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()

        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        mUserDetails = user
        hideProgressDialog()

        GlideLoader(this).loadUserPicture(user.image, iv_user_photo_settings)
        tv_name.text = "${user.firstname} ${user.lastname}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id) {

                R.id.tv_edit -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()

                        val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    finish()
                }
            }
        }
    }

}