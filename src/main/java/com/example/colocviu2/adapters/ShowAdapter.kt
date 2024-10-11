package com.example.colocviu2.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colocviu2.R
import com.example.colocviu2.data.Show
import com.example.colocviu2.ShowDetailsActivity


class ShowAdapter(
    private var shows: List<Show>

) : RecyclerView.Adapter<ViewHolder>() {


    fun setData(newShows: List<Show>) {
        this.shows = newShows
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]

        holder.showNameTextView.text = show.name

        val networkName = show.embedded?.show?.network?.name
        val webChannelName = show.embedded?.show?.webChannel?.name

        holder.showDetailsTextView.text = when {
            networkName != null -> networkName
            webChannelName != null -> webChannelName
            else -> "No available streaming platform"
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ShowDetailsActivity::class.java)
            intent.putExtra("show", show)
            context.startActivity(intent)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = shows.size


}

