package com.mwachakagrouplimited.mglic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.model.RestaurantModel
import android.widget.TextView

class OrderSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_succeess)
        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")

        //val actionBar = supportActionBar
        //actionBar!!.title = restaurantModel?.name
        //actionBar.subtitle = restaurantModel?.address
        //actionBar.setDisplayHomeAsUpEnabled(false)

        val buttonDone = findViewById<TextView>(R.id.buttonDone)
        buttonDone.setOnClickListener { finish() }
    }
}