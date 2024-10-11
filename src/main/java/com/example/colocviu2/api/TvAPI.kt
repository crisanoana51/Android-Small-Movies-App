package com.example.colocviu2.api

import com.example.colocviu2.data.Show
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvAPI {
    @GET("schedule")
    suspend fun getTodayShows(
        @Query("date") date: String,
        @Query("country") country: String
    ): List<Show>

    @GET("shows/{showId}")
    fun getShowDetails(@Path("showId") showId: Int): Call<Show>


    companion object {
        private const val BASE_URL = "https://api.tvmaze.com/"

        fun create(): TvAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TvAPI::class.java)
        }
    }
}