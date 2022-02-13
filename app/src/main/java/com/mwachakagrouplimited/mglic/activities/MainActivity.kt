package com.mwachakagrouplimited.mglic.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mwachakagrouplimited.mglic.R
import com.mwachakagrouplimited.mglic.adapters.RestaurantListAdapter
import com.mwachakagrouplimited.mglic.adapters.RestaurantListAdapter.RestaurantListClickListener
import com.mwachakagrouplimited.mglic.model.RestaurantModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*

class MainActivity : BaseActivity(), RestaurantListClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val restaurantModelList = restaurantData
        initRecyclerView(restaurantModelList)
    }

    private fun initRecyclerView(restaurantModelList: List<RestaurantModel>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantModelList, this)
        recyclerView.adapter = adapter
    }

    private val restaurantData: List<RestaurantModel>
        get() {
            val `is` = resources.openRawResource(R.raw.sauces)
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            try {
                val reader: Reader =
                    BufferedReader(InputStreamReader(`is`, "UTF-8"))
                var n: Int
                while (reader.read(buffer).also { n = it } != -1) {
                    writer.write(buffer, 0, n)
                }
            } catch (e: Exception) {
            }
            val jsonStr = writer.toString()
            val gson = Gson()
            val restaurantModels =
                gson.fromJson(
                    jsonStr,
                    Array<RestaurantModel>::class.java
                )
            return Arrays.asList(*restaurantModels)
        }

    override fun onItemClick(restaurantModel: RestaurantModel) {
        val intent = Intent(this@MainActivity, RestaurantMenuActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {

            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))

                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onBackPressed() {
        doubleBackToExit()
    }
}