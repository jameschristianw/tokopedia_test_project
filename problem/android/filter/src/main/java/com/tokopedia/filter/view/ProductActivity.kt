package com.tokopedia.filter.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tokopedia.filter.R
import com.tokopedia.filter.adapter.ProductAdapter
import com.tokopedia.filter.model.ProductData
import com.tokopedia.filter.model.ShopData
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ProductActivity : AppCompatActivity() {
    lateinit var rvProduct: RecyclerView
    lateinit var productAdapter: ProductAdapter
    lateinit var fabFilter: FloatingActionButton

    var products = ArrayList<ProductData>()
    var minMaxPrice = IntArray(2) { 0 }
    var shopLocationCount = hashMapOf<String, Int?>()

    val REQUEST_FILTER:Int = 1

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val selectedLocations = data?.getStringArrayExtra("SELECTED_LOCATIONS")
            val minPrice = data?.getIntExtra("MIN_PRICE", 0)
            val maxPrice = data?.getIntExtra("MAX_PRICE", 0)

            if (maxPrice != null) {
                if (minPrice != null) {
                    filterProduct(minPrice, maxPrice, selectedLocations)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            products.clear()
            processData()
            productAdapter = ProductAdapter(products)
            rvProduct.adapter = productAdapter
        }
    }

    private fun filterProduct(minPrice:Int, maxPrice:Int, cities: Array<String>?){
        products = if (cities != null && !cities.isEmpty()) {
            products.filter { product -> cities.contains(product.shopData.city) &&  product.priceInt!! in (minPrice + 1) until maxPrice + 1} as ArrayList<ProductData>
        } else {
            products.filter { it.priceInt!! in (minPrice + 1) until maxPrice + 1 } as ArrayList<ProductData>
        }
        productAdapter = ProductAdapter(products)
        rvProduct.adapter = productAdapter
    }

    private fun filterPage() {
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra("PRICES", minMaxPrice)
        intent.putExtra("LOCATIONS", shopLocationCount)

        Log.d("ProductActivity", "${minMaxPrice[0]} ${minMaxPrice[1]}")
        Log.d("ProductActivity", shopLocationCount.toString())

        startActivityForResult(intent, REQUEST_FILTER)
    }

    private fun getJsonDataFromAsset(): String {
        val stringBuffer = StringBuffer()
        val bufferReader: BufferedReader
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