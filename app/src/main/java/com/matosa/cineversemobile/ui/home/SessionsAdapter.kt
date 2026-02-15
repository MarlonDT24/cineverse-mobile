package com.matosa.cineversemobile.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.model.Session

class SessionsAdapter(
    private var sessions: List<Session>,
    private val onSessionClick: (Session) -> Unit
) : RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {

    class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tvSessionTime)
        val tvDate: TextView = view.findViewById(R.id.tvSessionDate)
        val tvCinema: TextView = view.findViewById(R.id.tvCinema)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        try {
            val rawDate = session.sessionDatetime.replace("T", " ")
            val dateTime = rawDate.split(" ")
            val datePart = dateTime[0]
            val timePart = dateTime[1].substring(0, 5)

            val dateSplit = datePart.split("-")
            val prettyDate = "${dateSplit[2]}/${dateSplit[1]}"

            holder.tvTime.text = timePart
            holder.tvDate.text = prettyDate
        } catch (e: Exception) {
            holder.tvTime.text = session.sessionDatetime
            holder.tvDate.text = "Fecha"
        }

        val cinemaName = session.cinema?.name ?: "Sala ${session.cinema?.id ?: "?"}"
        holder.tvCinema.text = cinemaName
        holder.tvPrice.text = "${session.priceNormal} €"

        holder.itemView.setOnClickListener { onSessionClick(session) }
    }

    override fun getItemCount() = sessions.size

    fun updateSessions(newSessions: List<Session>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}