package com.tokopedia.filter.model

data class ProductData (var id:Int, var name:String?, var imageUrl:String?, var priceInt:Int?, var discountPercentage:Int?, var slashPriceInt:Int?, var shopData: ShopData)