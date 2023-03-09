package com.example.bankuishapplication.Api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitClient {

        @GET("search/repositories")
        fun searchRepositories(
            @Query("q") query: String,
            @Query("per_page") perPage: Int,
            @Query("page") page: Int
        ): Call<JsonObject>

}