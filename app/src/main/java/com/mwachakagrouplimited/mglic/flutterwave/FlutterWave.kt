package com.mwachakagrouplimited.mglic.flutterwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mwachakagrouplimited.mglic.R
import com.flutterwave.raveandroid.RaveUiManager
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.flutterwave.raveandroid.RavePayActivity
import android.widget.Toast
import com.mwachakagrouplimited.mglic.activities.BaseActivity
import com.mwachakagrouplimited.mglic.model.User
import java.util.*

class FlutterWave : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User


    val amount_1 = 2
    var narration = "payment for sauces"
    var country = "KE"
    var currency = "KES"

    //var email = "mosesrng@gmail.com"
    //var lName = "LastName"
    var txRef: String? = null
    //var phoneNumber = "0759007522"

    val publicKey = "FLWPUBK_TEST-e43fabd275e28d51d7e22cc48189681a-X"
    val encryptionKey = "FLWSECK_TEST88c5238b8460"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)
        val btnOne = findViewById<TextView>(R.id.buttonPlaceYourOrder)
        btnOne.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id != R.id.buttonPlaceYourOrder) {
            return
        }
            makePayment(amount_1, user = User()) //calls payment method with amount 1
    }



    private fun makePayment(amount: Int, user: User) {
        txRef = user.email + " " + UUID.randomUUID().toString()
        RaveUiManager(this).setAmount(amount.toDouble())
            .setCountry(country)
            .setCurrency(currency)
            .setEmail(user.email)
            .setfName(user.firstname)
            .setlName(user.lastname)
            .setNarration(narration)
            .setPublicKey(publicKey)
            .setEncryptionKey(encryptionKey)
            .setTxRef(txRef)
            .setPhoneNumber(user.mobile.toString(), true)
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .acceptMpesaPayments(true) //.acceptAchPayments(false)
            .acceptGHMobileMoneyPayments(false) //.acceptUgMobileMoneyPayments(false)
            //.acceptZmMobileMoneyPayments(false)
            //.acceptRwfMobileMoneyPayments(false)
            //.acceptSaBankPayments(false)
            //.acceptUkPayments(false)
            .acceptBankTransferPayments(true)
            .acceptUssdPayments(true)
            .acceptBarterPayments(false) //.acceptFrancMobileMoneyPayments(false)
            .allowSaveCardFeature(true)
            .onStagingEnv(false) //.setMeta(List<Meta>)
            .withTheme(R.style.DefaultPayTheme) //.isPreAuth(false)
            //.setSubAccounts(List<SubAccount>)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED $message", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}