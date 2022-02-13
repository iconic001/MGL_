package com.mwachakagrouplimited.mglic.hover

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hover.sdk.api.Hover
import com.hover.sdk.api.HoverParameters
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_hover.*

class HoverActivity : AppCompatActivity() {

    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hover)

        Hover.initialize(this)

        val buttonSend = findViewById<Button>(R.id.buttonSend)

        buttonSend.setOnClickListener {
            if (editTextPhoneNumber.text.toString()
                    .isEmpty() || editTextAmount.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please Input both the phone Number and Amount",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            try {
                val intent = HoverParameters.Builder(this)
                    .request("eec4d4b1")  //Action ID
                    .extra("phoneNumber", editTextPhoneNumber.text.toString().trim())
                    .extra("amount", editTextAmount.text.toString().trim())
                    .buildIntent()

                startActivityForResult(intent, 0)
            } catch (e: Exception) {
                Toast.makeText(this@HoverActivity,
                    "Something went wrong please contact our support", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 0 && resultCode == RESULT_OK) {
                Toast.makeText(
                    this,
                    "You will receive an M-Pesa Confirmation message shortly",
                    Toast.LENGTH_SHORT
                ).show()
                finish()

            } else if (requestCode == 0 && resultCode == RESULT_CANCELED) {

                Toast.makeText(this, data?.getStringExtra("error"), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onActivityResult: ${data?.getStringExtra("error")}")
            }
        } catch (e: Exception) {
            Log.d(TAG, "onActivityResult: ${e.localizedMessage}")
        }
    }
}
