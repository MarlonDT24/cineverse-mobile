package com.matosa.cineversemobile.ui.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.model.Booking

class BookingsAdapter(
    private var bookings: List<Booking>,
    private val onBookingClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvMovieTitle)
        val tvCode: TextView = view.findViewById(R.id.tvBookingCode)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]

        val title = booking.movie?.title ?: booking.session?.cinema?.name ?: "Reserva CineVerse"

        holder.tvTitle.text = title
        holder.tvCode.text = "Ref: ${booking.bookingCode}"
        holder.tvStatus.text = booking.status

        val rawDate = booking.session?.sessionDatetime ?: booking.createdAt
        holder.tvDate.text = rawDate.replace("T", " ")

        holder.itemView.setOnClickListener { onBookingClick(booking) }
    }

    override fun getItemCount() = bookings.size

    fun updateBookings(newBookings: List<Booking>) {
        bookings = newBookings
        notifyDataSetChanged()
    }
}