package com.tokopedia.maps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryJSON {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: List<CountryModelDetail>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null
}