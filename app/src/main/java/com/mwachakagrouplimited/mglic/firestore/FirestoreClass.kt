package com.mwachakagrouplimited.mglic.firestore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mwachakagrouplimited.mglic.activities.LoginActivity
import com.mwachakagrouplimited.mglic.activities.RegisterActivity
import com.mwachakagrouplimited.mglic.activities.SettingsActivity
import com.mwachakagrouplimited.mglic.activities.UserProfileActivity
import com.mwachakagrouplimited.mglic.model.User
import com.mwachakagrouplimited.mglic.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                )
            }

    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    @SuppressLint("CommitPrefEdits")
    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MGL_PREFERENCES,
                        MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                if (user != null) {
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstname} ${user.lastname}"
                    )

                    editor.apply()
                }

                when (activity) {
                    is LoginActivity -> {

                        if (user != null) {
                            activity.userLoggedInSuccess(user)
                        }


                    }
                    is SettingsActivity -> {
                        if (user != null) {
                            activity.userDetailsSuccess(user)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()

                        Log.e(
                            activity.javaClass.simpleName,
                            "Error while Logging in.",
                        )
                    }
                    is SettingsActivity ->{
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {

                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when (activity) {
                    is UserProfileActivity -> {

                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + '.'
        + Constants.getFileExtension(
                activity,
                imageFileURI
        )
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e(
                "Firebase Image URI",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

           taskSnapshot.metadata!!.reference!!.downloadUrl
               .addOnSuccessListener { uri ->
                   Log.e("Downloadable Image URI", uri.toString())
                   when (activity) {
                       is UserProfileActivity -> {
                           activity.imageUploadSuccess(uri.toString())
                       }
                   }
               }
        }
            .addOnFailureListener{
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                /*Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )*/
            }
    }

}