package com.tokopedia.filter.view

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import com.tokopedia.filter.R
import java.text.DecimalFormat
import java.text.NumberFormat

class FilterActivity : AppCompatActivity() {
    lateinit var tvMinPrice: TextView
    lateinit var tvMaxPrice: TextView
    lateinit var cpLocation: ChipGroup
    lateinit var btnFilter: Button
    lateinit var btnResetFilter: Button
    lateinit var seekbarMinPrice: SeekBar
    lateinit var seekbarMaxPrice: SeekBar
//    lateinit var sliderMinPrice: Slider
//    lateinit var sliderMaxPrice: Slider

    var selectedLocations = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        title = "Filter Produk"

        tvMinPrice = findViewById(R.id.tvMinPrice)
        tvMaxPrice = findViewById(R.id.tvMaxPrice)
        cpLocation = findViewById(R.id.cpLocation)
        btnFilter = findViewById(R.id.btnFilter)
        btnResetFilter = findViewById(R.id.btnResetFilter)
        seekbarMinPrice = findViewById(R.id.seekbarMinPrice)
        seekbarMaxPrice = findViewById(R.id.seekbarMaxPrice)
//        sliderMinPrice = findViewById(R.id.sliderMinPrice)
//        sliderMaxPrice = findViewById(R.id.sliderMaxPrice)

        val nf: NumberFormat = DecimalFormat("#,###")

        val minMaxPrice: IntArray? = intent.getIntArrayExtra("PRICES")
        val minPrice = minMaxPrice?.get(0)
        val maxPrice = minMaxPrice?.get(1)
        tvMinPrice.text = "Rp ${nf.format(minPrice)}"
        tvMaxPrice.text = "Rp ${nf.format(maxPrice)}"

        // Seekbar for minimum price
        seekbarMinPrice.max = maxPrice ?: 0
        seekbarMinPrice.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 < minPrice!!) tvMinPrice.text = "Rp ${nf.format(minPrice)}"
                else tvMinPrice.text = "Rp ${nf.format(p1)}"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        // Seekbar for maximum price
        seekbarMaxPrice.max = maxPrice!!
        seekbarMaxPrice.progress = maxPrice
        seekbarMaxPrice.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 < minPrice!!) tvMaxPrice.text = "Rp ${nf.format(minPrice)}"
                else tvMaxPrice.text = "Rp ${nf.format(p1)}"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        val locations = intent.getSerializableExtra("LOCATIONS") as Map<String, Int>
        val sortedLocations = locations.toList().sortedBy { (_, value) -> value }.reversed().toMap()
        Log.d("FilterActivity", sortedLocations.toString())

        createLocationChips(sortedLocations)

        btnFilter.setOnClickListener{ setFilterParameter() }
        btnResetFilter.setOnClickListener{ setResetFilterParameter() }

        // Slider for minimum price
//        sliderMinPrice.setRange(minPrice.toFloat(), maxPrice.toFloat())
//        sliderMinPrice.setOnChangeListener{ _, value -> tvMinPrice.text = value.toInt().toString() }

        // Slider for maximum price
//        sliderMaxPrice.setRange(minPrice.toFloat(), maxPrice.toFloat())
//        sliderMaxPrice.value = maxPrice.toFloat()
    }

    private fun setResetFilterParameter() {
        val intentFilter = Intent(this, ProductActivity::class.java)
        setResult(Activity.RESULT_CANCELED, intentFilter)
        finish()
    }

    private fun createLocationChips(locations:Map<String, Int>){
        for (location in locations){
            val newChip = Chip(this)
            newChip.text = location.key
            newChip.isClickable = true
            newChip.isCloseIconVisible = false
            newChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))

            newChip.setOnClickListener{
                Log.d("FilterActivity", location.key)
                if (selectedLocations.isEmpty() || !selectedLocations.contains(location.key)) {
                    selectedLocations.add(location.key)
                    newChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))
                }
                else {
                    selectedLocations.remove(location.key)
                    newChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))
                }

                Log.d("FilterActivity", selectedLocations.toString())
            }

            cpLocation.addView(newChip)
        }
    }

    private fun setFilterParameter(){
        val intentFilter = Intent(this, ProductActivity::class.java)
        intentFilter.putExtra("MIN_PRICE", seekbarMinPrice.progress)
        intentFilter.putExtra("MAX_PRICE", seekbarMaxPrice.progress)
        intentFilter.putExtra("SELECTED_LOCATIONS", selectedLocations.toTypedArray())
        setResult(Activity.RESULT_OK, intentFilter)
        finish()
    }

//    private fun Slider.setRange(from: Float, to: Float) {
//        valueFrom = 0F
//        valueTo = to
//        valueFrom = from
//    }
}