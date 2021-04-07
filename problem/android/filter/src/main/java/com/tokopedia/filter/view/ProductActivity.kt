package com.tokopedia.filter.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.filter.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Filter

class ProductActivity : AppCompatActivity() {
    var products = ArrayList<ProductData>()
    lateinit var rvProduct: RecyclerView
    lateinit var productAdapter: ProductAdapter
    lateinit var fabFilter: FloatingActionButton
    var minMaxPrice = IntArray(2) { 0 }
    var shopLocationCount = hashMapOf<String, Int?>()
    var locationCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        fabFilter = findViewById(R.id.fabFilter)

        processData()

        rvProduct = findViewById(R.id.product_list)
        productAdapter = ProductAdapter(products)
        rvProduct.layoutManager = GridLayoutManager(this, 2)
        rvProduct.adapter = productAdapter

        fabFilter.setOnClickListener{ filterPage() }
    }

    private fun filterPage() {
        Log.d("ProductActivity", "Test Filter")
        var intent = Intent(this, FilterActivity::class.java)
        intent.putExtra("PRICES", minMaxPrice)
        intent.putExtra("LOCATIONS", shopLocationCount)

        Log.d("ProductActivity", "${minMaxPrice[0]} ${minMaxPrice[1]}")
        Log.d("ProductActivity", shopLocationCount.toString())

        startActivity(intent)
    }

    private fun getJsonDataFromAsset(): String {
        val stringBuffer = StringBuffer()
        var bufferReader: BufferedReader
        try{
            bufferReader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.products)))
            bufferReader.readLines().map { line -> stringBuffer.append(line) }
        } catch (ioException: IOException){
            ioException.printStackTrace()
            return ""
        }

        return stringBuffer.toString()
    }

    private fun processData(){
        val jsonString: String = getJsonDataFromAsset()
        val jsonObject = JSONObject(jsonString)
        val jsonData = jsonObject.getJSONObject("data")
        val jsonArrayProduct = jsonData.getJSONArray("products")


        for (i in 0 until jsonArrayProduct.length()){
            val productDetail = jsonArrayProduct.getJSONObject(i);
            val shopJson = productDetail["shop"] as JSONObject

            val shopID = shopJson["id"].toString().toInt()
            val shopName = shopJson["name"].toString()
            val shopCity = shopJson["city"].toString()
            val shopData = ShopData(shopID, shopName, shopCity)

            val productID = productDetail["id"].toString().toInt()
            val productName = productDetail["name"].toString()
            val productImageUrl = productDetail["imageUrl"].toString()
            val productPriceInt = productDetail["priceInt"].toString().toInt()
            val productDiscountPercentage = productDetail["discountPercentage"].toString().toInt()
            val productSlashedPriceInt = productDetail["slashedPriceInt"].toString().toInt()

            val newProduct = ProductData(productID, productName, productImageUrl, productPriceInt, productDiscountPercentage, productSlashedPriceInt, shopData)

            setMinMaxPrice(productPriceInt)
            shopData.city?.let { countLocation(it) }

            products.add(newProduct)
        }
    }

    private fun setMinMaxPrice(price: Int){
        if(minMaxPrice[0] == 0){
            minMaxPrice[0] = price
            return
        }
        if(minMaxPrice[1] == 0){
            minMaxPrice[1] = price
            return
        }

        if(price < minMaxPrice[0]) minMaxPrice[0] = price
        else if(price > minMaxPrice[1]) minMaxPrice[1] = price
    }

    private fun countLocation(location:String){
        if (!shopLocationCount.containsKey(location)) shopLocationCount[location] = 1
        else {
            var temp:Int? = shopLocationCount[location]
            if (temp != null) {
                temp += 1
            }
            shopLocationCount[location] = temp
        }
    }

    private fun printDataFromObject(){
        println("Printing data from object")
        for (product in products) {
            println(product.shopData.name)
        }
    }
}