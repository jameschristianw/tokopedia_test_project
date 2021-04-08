package com.tokopedia.filter.adapter

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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.model.ProductData
import java.io.InputStream
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat


class ProductAdapter(private var products: List<ProductData>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: ProductData = products[position]
        val nf:NumberFormat = DecimalFormat("#,###")

        DownloadImageTask(holder.imgProductImage, holder.progressCircular).execute(product.imageUrl)

        holder.tvProductName.text = product.name
        holder.tvProductPrice.text = "Rp ${nf.format(product.priceInt)}"
        holder.tvProductPrice.typeface = Typeface.DEFAULT_BOLD

        if(product.discountPercentage != 0) {
            holder.tvProductSlashedPrice.text = "Rp ${nf.format(product.slashPriceInt)}"
            holder.tvProductDiscount.text = "${product.discountPercentage.toString()}%"
            holder.tvProductSlashedPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.llDiscount.visibility = View.VISIBLE
        } else {
            holder.llDiscount.visibility = View.GONE
        }

        holder.tvProductShopCity.text = product.shopData.city
    }

    private class DownloadImageTask(var bmImage: ImageView, var progress: ProgressBar) : AsyncTask<String?, Void?, Bitmap?>() {

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
            progress.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llDiscount: LinearLayout = view.findViewById(R.id.llDiscount)
        var tvProductName: TextView = view.findViewById(R.id.tvProductName)
        var tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        var tvProductSlashedPrice: TextView = view.findViewById(R.id.tvProductSlashedPrice)
        var tvProductDiscount: TextView = view.findViewById(R.id.tvProductDiscountPercent)
        var tvProductShopCity: TextView = view.findViewById(R.id.tvProductShopCity)
        var imgProductImage: ImageView = view.findViewById(R.id.imgProductImage)
        var progressCircular: ProgressBar = view.findViewById(R.id.progressCircular)

    }
}