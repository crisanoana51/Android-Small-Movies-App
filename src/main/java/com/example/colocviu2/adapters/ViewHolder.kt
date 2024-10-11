package com.example.colocviu2.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colocviu2.R
import com.example.colocviu2.data.Show

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val showNameTextView: TextView = itemView.findViewById(R.id.showNameTextView)
    val showDetailsTextView: TextView = itemView.findViewById(R.id.showDetailsTextView)


    fun bind(show: Show) {

        showNameTextView.text = show.name

        showDetailsTextView.text = when {
            show.embedded.show.network?.name != null -> show.embedded.show.network.name
            show.embedded.show.webChannel?.name != null -> show.embedded.show.webChannel.name
            else -> "No available streaming platform"
        }

    }
}