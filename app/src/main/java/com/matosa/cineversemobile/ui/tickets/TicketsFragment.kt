package com.matosa.cineversemobile.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.databinding.FragmentTicketsBinding
import com.matosa.cineversemobile.model.Booking
import com.matosa.cineversemobile.model.Movie

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTickets.layoutManager = LinearLayoutManager(context)

        val adapter = BookingsAdapter(emptyList()) { booking ->
            val bundle = Bundle().apply {
                putSerializable("booking_data", booking)
            }
            // Importar: androidx.navigation.fragment.findNavController
            findNavController().navigate(R.id.action_ticketsFragment_to_ticketDetailFragment, bundle)        }
        binding.rvTickets.adapter = adapter

        cargarBookingsFalsas(adapter)
    }

    private fun cargarBookingsFalsas(adapter: BookingsAdapter) {
        val listaFalsa = listOf(
            Booking(
                id = 1,
                bookingCode = "CV20250203001",
                totalAmount = 17.00,
                status = "CONFIRMED",
                createdAt = "2025-02-03",
                session = null,
                // Simulamos la peli dentro
                movie = Movie(1, "Avatar: El Camino del Agua", "", "", "", 192, "")
            ),
            Booking(
                id = 2,
                bookingCode = "CV20250204005",
                totalAmount = 25.50,
                status = "CONFIRMED",
                createdAt = "2025-02-04",
                session = null,
                movie = Movie(2, "Star Wars: Episodio IV", "", "", "", 120, "")
            )
        )
        adapter.updateBookings(listaFalsa)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}