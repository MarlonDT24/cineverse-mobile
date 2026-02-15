package com.matosa.cineversemobile.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.model.StarWarsEvent

class StarWarsAdapter(private val events: List<StarWarsEvent>) :
    RecyclerView.Adapter<StarWarsAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvEventTitle)
        val character: TextView = view.findViewById(R.id.tvCharacterName)
        val description: TextView = view.findViewById(R.id.tvEventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_star_wars_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.title.text = event.title
        holder.character.text = "Invitado: ${event.characterName}"
        holder.description.text = event.description
    }

    override fun getItemCount() = events.size
}