package com.matosa.cineversemobile.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.model.Seat

class SeatsAdapter(
    private var seats: List<Seat>,
    private val onSeatClick: (Seat) -> Unit
) : RecyclerView.Adapter<SeatsAdapter.SeatViewHolder>() {

    class SeatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSeat: TextView = view.findViewById(R.id.tvSeatNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seats[position]

        holder.tvSeat.text = "${seat.row}${seat.number}"

        // --- LÓGICA DE LOS COLORES ---
        when {
            seat.isBooked -> {
                holder.tvSeat.setBackgroundResource(R.drawable.bg_seat_booked)
                holder.tvSeat.isEnabled = false
                holder.tvSeat.text = "X"
            }
            seat.isSelected -> {
                holder.tvSeat.setBackgroundResource(R.drawable.bg_seat_selected)
                holder.tvSeat.isEnabled = true
                holder.tvSeat.text = "${seat.price}€"
            }
            else -> {
                // Si está libre, miramos si es VIP o NORMAL
                if (seat.type == "VIP") {
                    holder.tvSeat.setBackgroundResource(R.drawable.bg_seat_vip)
                } else {
                    holder.tvSeat.setBackgroundResource(R.drawable.bg_seat_available)
                }
                holder.tvSeat.isEnabled = true
                holder.tvSeat.text = "${seat.row}${seat.number}"
            }
        }

        holder.itemView.setOnClickListener {
            if (!seat.isBooked) {
                seat.isSelected = !seat.isSelected // Invertir estado
                notifyItemChanged(position) // Refrescar solo este cuadradito
                onSeatClick(seat) // Avisar al fragmento
            }
        }
    }

    override fun getItemCount() = seats.size

    fun updateSeats(newSeats: List<Seat>) {
        seats = newSeats
        notifyDataSetChanged()
    }
}