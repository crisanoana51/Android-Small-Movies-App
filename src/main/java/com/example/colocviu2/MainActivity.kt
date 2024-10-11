package com.example.colocviu2

import com.example.colocviu2.adapters.ShowAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colocviu2.api.TvAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var showAdapter: ShowAdapter
    private lateinit var tvMazeApiService: TvAPI
    private lateinit var toolbar: Toolbar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(this.toolbar)

        recyclerView = findViewById(R.id.showsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        showAdapter = ShowAdapter(emptyList())
        recyclerView.adapter = showAdapter


        val httpInterceptor = HttpLoggingInterceptor().apply {

            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder().apply {
            this.addInterceptor ( httpInterceptor )
        }.build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client ( httpClient )
            .build()

        tvMazeApiService = retrofit.create(TvAPI::class.java)

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.d("MainActivity", currentDate)
        val country = "US"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val shows = tvMazeApiService.getTodayShows(currentDate, country)
                withContext(Dispatchers.Main) {
                    showAdapter.setData(shows)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_date -> {
                showDatePickerDialog()
                true
            }
            R.id.action_today -> {
                // Handle "Today" option
                fetchAndDisplayShows(getCurrentDate())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date(year - 1900, month, dayOfMonth))

                fetchAndDisplayShows(selectedDate)
            },
            initialYear,
            initialMonth,
            initialDay
        )

        datePickerDialog.show()
    }

    private fun fetchAndDisplayShows(date: String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val shows = tvMazeApiService.getTodayShows(date, "US")
                withContext(Dispatchers.Main) {
                    showAdapter.setData(shows)
                }
            } catch (e: Exception) {
                // Handle error
                withContext(Dispatchers.Main) {
                    showToast("Error fetching shows")
                }
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}