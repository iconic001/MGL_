package com.mwachakagrouplimited.mglic.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.firestore.FirestoreClass
import com.mwachakagrouplimited.mglic.model.User
import com.mwachakagrouplimited.mglic.utils.Constants
import com.mwachakagrouplimited.mglic.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.tv_title
import java.io.IOException


class UserProfileActivity : BaseActivity(), View.OnClickListener {

   private lateinit var mUserDetails: User
   private var mSelectedImageFileUri: Uri? = null
    private var mUserprofileImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name_profile.setText(mUserDetails.firstname)
        et_last_name_profile.setText(mUserDetails.lastname)
        et_email_profile.isEnabled = false
        et_email_profile.setText(mUserDetails.email)

        GlideLoader(this).loadUserPicture(mUserDetails.image, iv_user_photo)

        if(mUserDetails.profileCompleted == 0) {
            tv_title.text = resources.getString(R.string.title_complete_profile)
            et_first_name_profile.isEnabled = false
            et_last_name_profile.isEnabled = false


        }else {
            setupActionBar()
            tv_title.text = resources.getString(R.string.title_edit_profile)

            GlideLoader(this).loadUserPicture(mUserDetails.image, iv_user_photo)

            if (mUserDetails.mobile != 0L) {
                et_mobile_number_profile.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                rb_male.isChecked = true
            }else {
                rb_female.isChecked = true
            }
        }

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED

                    ){
                        //showErrorSnackBar("You already have the storage permission,",false)

                        Constants.showImageChooser(this)

                    }else{
                      ActivityCompat.requestPermissions(
                          this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                          Constants.READ_STORAGE_PERMISSION_CODE
                      )
                    }
                }

                R.id.btn_submit -> {


                  if (validateUserProfileDetails()){

                      showProgressDialog(resources.getString(R.string.please_wait))

                      if (mSelectedImageFileUri != null)
                          FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                      else{
                          updateUserprofileDetails()
                      }
                  }
                }
            }
        }
    }

    private fun updateUserprofileDetails() {

        val userHashMap = HashMap <String, Any> ()

        val firstName = et_first_name_profile.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstname) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = et_last_name_profile.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastname) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = et_mobile_number_profile.text.toString().trim { it <= ' ' }

        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if (mUserprofileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserprofileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this, userHashMap)
        //showErrorSnackBar("Your details have been saved!",false)

    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(this,resources.getString(R.string.msg_profile_update_success),
        Toast.LENGTH_LONG).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //showErrorSnackBar("The storage permission is granted.", false)

                Constants.showImageChooser(this)

            }else{
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {


                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))

                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this, resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){

            Log.e("Request canceled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number_profile.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()

        mUserprofileImageURL = imageURL
        updateUserprofileDetails()
    }
}