package com.example.colocviu2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.colocviu2.api.TvAPI
import com.example.colocviu2.data.Show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowDetailsActivity : AppCompatActivity() {


    private lateinit var showNameTextView: TextView
    private lateinit var showDetailsTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var genresTextView: TextView
    private lateinit var averageRuntimeTextView: TextView
    private lateinit var imageView: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        showNameTextView = findViewById(R.id.showDetailsNameTextView)
        showDetailsTextView = findViewById(R.id.showDetailsDescriptionTextView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        val show = intent.getSerializableExtra("show") as? Show
        show?.let {
            showLoadingIndicator()
            Handler().postDelayed({
                fetchShowDetails(it.id)
            }, 2000)
        }
    }

    private fun fetchShowDetails(showId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("<BASE_URL>")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val tvMazeApiService = retrofit.create(TvAPI::class.java)

        val call = tvMazeApiService.getShowDetails(showId)
        call.enqueue(object : Callback<Show> {
            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                hideLoadingIndicator()
                if (response.isSuccessful) {
                    val showDetails = response.body()
                    showDetails?.let { displayShowDetails(it) }
                }
            }

            override fun onFailure(call: Call<Show>, t: Throwable) {
                hideLoadingIndicator()

            }
        })
    }

    private fun displayShowDetails(showDetails: Show) {
        if (!showDetails.name.isNullOrEmpty()) {
            showNameTextView.text = showDetails.name
        } else {
            showNameTextView.visibility = View.GONE
        }

        if (!showDetails.embedded.show.genres.isNullOrEmpty()) {
            val genresText = showDetails.embedded.show.genres.joinToString(", ")
            genresTextView.text = genresText
        } else {
            genresTextView.visibility = View.GONE
        }


        if (showDetails.embedded.show.averageRuntime != null) {
            averageRuntimeTextView.text = "Average Runtime: ${showDetails.embedded.show.averageRuntime} minutes"
        } else {
            averageRuntimeTextView.visibility = View.GONE
        }


        if (!showDetails.summary.isNullOrEmpty()) {
            showDetailsTextView.text = showDetails.summary
        } else {
            showDetailsTextView.visibility = View.GONE
        }


        if (showDetails.image != null) {
            val imageUrl = showDetails.image.medium ?: showDetails.image.original
            if (imageUrl != null) {
                loadImage(imageUrl)
            }
        } else {

            imageView.visibility = View.GONE
        }

    }

    private fun showLoadingIndicator() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        loadingProgressBar.visibility = View.INVISIBLE
    }
    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }

}
