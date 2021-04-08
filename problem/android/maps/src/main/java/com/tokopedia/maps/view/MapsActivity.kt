package com.tokopedia.maps.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.tokopedia.maps.CountryJSON
import com.tokopedia.maps.CountryModelDetail
import com.tokopedia.maps.NetworkConfig
import com.tokopedia.maps.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

open class MapsActivity : AppCompatActivity() {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private lateinit var textCountryName: TextView
    private lateinit var textCountryCapital: TextView
    private lateinit var textCountryPopulation: TextView
    private lateinit var textCountryCallCode: TextView

    private var editText: EditText? = null
    private var buttonSubmit: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        bindViews()
        initListeners()
        loadMap()
    }

    private fun bindViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editText = findViewById(R.id.editText)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        textCountryName = findViewById(R.id.txtCountryName)
        textCountryCapital = findViewById(R.id.txtCountryCapital)
        textCountryPopulation = findViewById(R.id.txtCountryPopulation)
        textCountryCallCode = findViewById(R.id.txtCountryCallCode)
    }

    private fun initListeners() {
        buttonSubmit!!.setOnClickListener {
            // TODO
            // search by the given country name, and
            val locale: String? = getCountryCode((editText?.text.toString()).capitalize())

            if (locale != null) {
                NetworkConfig().getService().getCountryDetail(locale).enqueue(object: Callback<CountryJSON> {
                    override fun onResponse(call: Call<CountryJSON>, response: Response<CountryJSON>) {
                        val countryObject: CountryJSON? = response.body()

                        // 1. pin point to the map
                        val lat = countryObject?.data?.get(0)?.latitude?.toDouble()
                        val lng = countryObject?.data?.get(0)?.longitude?.toDouble()
                        if (lat != null && lng != null){
                            val latLng = LatLng(lat, lng)
                            mapFragment!!.getMapAsync {
                                googleMap?.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)))
                                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                            }
                        }

                        // 2. set the country information to the textViews.
                        if (countryObject != null) {
                            countryObject.data?.get(0)?.let { it1 -> resultFormat(it1) }
                        }

                        // Tadinya saya mau bikin dia ambil 2 API
                        // 1 untuk cari locale code negara (bisa search negara)
                        // 1 nya lagi yang dipakai sekarang
                        // Tapi setelah saya pikir-pikir dan diskusi sama beberapa teman saya
                        // Kita sepakat kalau itu bukan best practice
                    }

                    override fun onFailure(call: Call<CountryJSON>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(applicationContext, "Periksa koneksi internet Anda!", Toast.LENGTH_LONG).show()
                    }

                })
            } else {
                Toast.makeText(this, "Masukkan nama negara dengan benar dan lengkap dalam Bahasa Inggris", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resultFormat(detail: CountryModelDetail) {
        textCountryName.text = "Nama negara: ${detail.name}"
        textCountryCapital.text = "Ibukota: ${detail.capital}"
        textCountryPopulation.text = "Jumlah penduduk: ${detail.population}"
        textCountryCallCode.text = "Kode telepon: ${detail.phone}"
    }

    private fun getCountryCode(countryName: String): String? =
            Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    fun loadMap() {
        mapFragment!!.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }
}
