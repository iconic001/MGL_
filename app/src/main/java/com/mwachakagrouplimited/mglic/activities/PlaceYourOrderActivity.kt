package com.mwachakagrouplimited.mglic.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hover.sdk.api.HoverParameters
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.adapters.PlaceYourOrderAdapter
import com.mwachakagrouplimited.mglic.hover.HoverActivity
import com.mwachakagrouplimited.mglic.model.RestaurantModel
import kotlinx.android.synthetic.main.activity_place_your_order.*

class PlaceYourOrderActivity : AppCompatActivity() {

    val amount_1 = 2
    var narration = "payment for sauces"
    var country = "KE"
    var currency = "KES"
    var fName = "FirstName"
    var email = "mosesrng@gmail.com"
    var lName = "LastName"
    var txRef: String? = null
    var phoneNumber = "0759007522"


    val publicKey = "FLWPUBK_TEST-e43fabd275e28d51d7e22cc48189681a-X"
    val encryptionKey = "FLWSECK_TEST88c5238b8460"


    private var inputName: EditText? = null
    private var inputAddress: EditText? = null
    private var inputCity: EditText? = null
    private var inputState: EditText? = null
    private var inputZip: EditText? = null
    private var inputCardNumber: EditText? = null
    private var inputCardExpiry: EditText? = null
    private var inputCardPin: EditText? = null
    private var cartItemsRecyclerView: RecyclerView? = null
    private var tvSubtotalAmount: TextView? = null
    private var tvDeliveryChargeAmount: TextView? = null
    private var tvDeliveryCharge: TextView? = null
    private var tvTotalAmount: TextView? = null
    private var buttonPlaceYourOrder: TextView? = null
    private var switchDelivery: SwitchCompat? = null
    private var isDeliveryOn = false
    private var placeYourOrderAdapter: PlaceYourOrderAdapter? = null

    private val TAG = "PlaceYourOrderActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)
        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        supportActionBar



        //actionBar!!.title = restaurantModel?.name
        //actionBar.subtitle = restaurantModel?.address
        //actionBar.setDisplayHomeAsUpEnabled(true)
        //inputName = findViewById(R.id.inputName)
        //inputAddress = findViewById(R.id.inputAddress)
        //inputCity = findViewById(R.id.inputCity)
        //inputState = findViewById(R.id.inputState)
        //inputZip = findViewById(R.id.inputZip)
        //inputCardNumber = findViewById(R.id.inputCardNumber)
        //inputCardExpiry = findViewById(R.id.inputCardExpiry)
        //inputCardPin = findViewById(R.id.inputCardPin)
        //tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount)
        //tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount)
        //tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge)
        //tvTotalAmount = findViewById(R.id.tvTotalAmount)
        //buttonPlaceYourOrder = findViewById<TextView>(R.id.buttonPlaceYourOrder)
        //switchDelivery = findViewById(R.id.switchDelivery)
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView)
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder)
        buttonPlaceYourOrder?.setOnClickListener {

            val intent = Intent(this@PlaceYourOrderActivity, HoverActivity::class.java)
            intent.putExtra("RestaurantModel", restaurantModel)
            startActivityForResult(intent, 1000)
            onPlaceOrderButtonClick(
                restaurantModel
            )
        }
        switchDelivery?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                inputAddress?.visibility = View.VISIBLE
                inputCity?.visibility = View.VISIBLE
                inputState?.visibility = View.VISIBLE
                inputZip?.visibility = View.VISIBLE
                tvDeliveryChargeAmount?.visibility = View.VISIBLE
                tvDeliveryCharge?.visibility = View.VISIBLE
                isDeliveryOn = true
                calculateTotalAmount(restaurantModel)
            } else {
                inputAddress?.visibility = View.GONE
                inputCity?.visibility = View.GONE
                inputState?.visibility = View.GONE
                inputZip?.visibility = View.GONE
                tvDeliveryChargeAmount?.visibility = View.GONE
                tvDeliveryCharge?.visibility = View.GONE
                isDeliveryOn = false
                calculateTotalAmount(restaurantModel)
            }
        }
        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)
    }

    @SuppressLint("SetTextI18n")
     fun calculateTotalAmount(restaurantModel: RestaurantModel?) {
        var subTotalAmount = 0f
        for (m in restaurantModel!!.menus) {
            subTotalAmount += m.price * m.totalInCart
        }
        tvSubtotalAmount = findViewById(R.id.tvSubtotal)
        tvSubtotalAmount!!.text = "KSH" + String.format("%.2f", subTotalAmount)
        if (isDeliveryOn) {
            tvDeliveryChargeAmount!!.text =
                "KSH" + String.format("%.2f", restaurantModel.delivery_charge)
            subTotalAmount += restaurantModel.delivery_charge
        }
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTotalAmount!!.text = "KSH" + String.format("%.2f", subTotalAmount)
    }


    private fun onPlaceOrderButtonClick(restaurantModel: RestaurantModel?) {





        /*if (TextUtils.isEmpty(inputName!!.text.toString())) {
            inputName!!.error = "Please enter name "
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputAddress!!.text.toString())) {
            inputAddress!!.error = "Please enter address "
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputCity!!.text.toString())) {
            inputCity!!.error = "Please enter city "
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputState!!.text.toString())) {
            inputState!!.error = "Please enter zip "
            return
        } else if (TextUtils.isEmpty(inputCardNumber!!.text.toString())) {
            inputCardNumber!!.error = "Please enter card number "
            return
        } else if (TextUtils.isEmpty(inputCardExpiry!!.text.toString())) {
            inputCardExpiry!!.error = "Please enter card expiry "
            return
        } else if (TextUtils.isEmpty(inputCardPin!!.text.toString())) {
            inputCardPin!!.error = "Please enter card pin/cvv "
            return
        }*/
        //start success activity..
        //val intent = Intent(this@PlaceYourOrderActivity, HoverActivity::class.java)
        //intent.putExtra("RestaurantModel", restaurantModel)
        //startActivityForResult(intent, 1000)
    }

    private fun initRecyclerView(restaurantModel: RestaurantModel?) {
        cartItemsRecyclerView!!.layoutManager = LinearLayoutManager(this)
        placeYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel!!.menus)
        cartItemsRecyclerView!!.adapter = placeYourOrderAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000) {
            setResult(RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } //do nothing
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}