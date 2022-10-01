package com.example.flixster

import com.google.gson.annotations.SerializedName

class Movie {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("poster_path")
    var poster_path: String? = null

    @SerializedName("overview")
    var overview: String? = null

}