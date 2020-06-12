package com.test.weather.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("main")
    @Expose
    lateinit var main: String
    @SerializedName("description")
    @Expose
    lateinit var description: String
    @SerializedName("icon")
    @Expose
    var icon: String? = null

}