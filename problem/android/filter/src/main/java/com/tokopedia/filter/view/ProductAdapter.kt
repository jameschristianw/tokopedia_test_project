package com.tokopedia.filter.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import java.io.InputStream
import java.lang.reflect.Array
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class ProductAdapter(private val products: List<ProductData>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var minMaxPrice = IntArray(2) { 0 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: ProductData = products[position]
        val nf:NumberFormat = DecimalFormat("#,###")

//        product.priceInt?.let { setMinMaxPrice(it) };

        DownloadImageTask(holder.imgProductImage).execute(product.imageUrl)

        holder.tvProductName.text = product.name
//        holder.tvProductName.typeface = Typeface.DEFAULT_BOLD

//        holder.tvProductPrice.text = "Rp ${product.priceInt.toString()}"
        holder.tvProductPrice.text = "Rp ${nf.format(product.priceInt)}"
        holder.tvProductPrice.typeface = Typeface.DEFAULT_BOLD

        if(product.discountPercentage != 0) {
//            holder.tvProductSlashedPrice.text = "Rp ${product.slashPriceInt.toString()}"
            holder.tvProductSlashedPrice.text = "Rp ${nf.format(product.slashPriceInt)}"
            holder.tvProductDiscount.text = "${product.discountPercentage.toString()}%"
            holder.tvProductSlashedPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.llDiscount.visibility = View.VISIBLE
        } else {
            holder.llDiscount.visibility = View.GONE
        }

        holder.tvProductShopCity.text = product.shopData.city
    }

    private class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg urls: String?): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val input: InputStream = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

    }

//    private fun setMinMaxPrice(price: Int){
//        if(minMaxPrice[0] == 0){
//            minMaxPrice[0] = price
//        }
//        if(minMaxPrice[1] == 0){
//            minMaxPrice[1] = price
//        }
//
//        if(minMaxPrice[0] > price){
//            if(minMaxPrice[0] > minMaxPrice[1]) minMaxPrice[1] = minMaxPrice[0];
//            minMaxPrice[0] = price
//        }
//    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llDiscount: LinearLayout = view.findViewById(R.id.llDiscount)
        var tvProductName: TextView = view.findViewById(R.id.tvProductName)
        var tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        var tvProductSlashedPrice: TextView = view.findViewById(R.id.tvProductSlashedPrice)
        var tvProductDiscount: TextView = view.findViewById(R.id.tvProductDiscountPercent)
        var tvProductShopCity: TextView = view.findViewById(R.id.tvProductShopCity)
        var imgProductImage: ImageView = view.findViewById(R.id.imgProductImage)
    }

    fun getPrices() : IntArray {
        return minMaxPrice
    }
}