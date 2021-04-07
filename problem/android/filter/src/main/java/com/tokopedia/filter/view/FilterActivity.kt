package com.tokopedia.filter.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
//    lateinit var sliderMinPrice: Slider
//    lateinit var sliderMaxPrice: Slider
    lateinit var seekbarMinPrice: SeekBar
    lateinit var seekbarMaxPrice: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        title = "Filter Produk"

        tvMinPrice = findViewById(R.id.tvMinPrice)
        tvMaxPrice = findViewById(R.id.tvMaxPrice)
        cpLocation = findViewById(R.id.cpLocation)
        btnFilter = findViewById(R.id.btnFilter)
//        sliderMinPrice = findViewById(R.id.sliderMinPrice)
//        sliderMaxPrice = findViewById(R.id.sliderMaxPrice)
        seekbarMinPrice = findViewById(R.id.seekbarMinPrice)
        seekbarMaxPrice = findViewById(R.id.seekbarMaxPrice)

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

        // Slider for minimum price
//        sliderMinPrice.setRange(minPrice.toFloat(), maxPrice.toFloat())
//        sliderMinPrice.setOnChangeListener{ _, value -> tvMinPrice.text = value.toInt().toString() }

        // Slider for maximum price
//        sliderMaxPrice.setRange(minPrice.toFloat(), maxPrice.toFloat())
//        sliderMaxPrice.value = maxPrice.toFloat()

        val locations = intent.getSerializableExtra("LOCATIONS") as Map<String, Int>
        val sortedLocations = locations.toList().sortedBy { (_, value) -> value }.reversed().toMap()
        Log.d("FilterActivity", sortedLocations.toString())
    }

//    private fun Slider.setRange(from: Float, to: Float) {
//        valueFrom = 0F
//        valueTo = to
//        valueFrom = from
//    }
}