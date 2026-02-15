package com.matosa.cineversemobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.matosa.cineversemobile.databinding.FragmentSeatsBinding
import com.matosa.cineversemobile.model.Movie
import com.matosa.cineversemobile.model.Session
import com.matosa.cineversemobile.R

class SeatsFragment : Fragment() {

    private var _binding: FragmentSeatsBinding? = null
    private val binding get() = _binding!!
    private var session: Session? = null
    private var movie: Movie? = null

    private val viewModel: SeatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            session = it.getSerializable("session_data") as Session?
            movie = it.getSerializable("movie_data") as Movie?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session?.let {
            val fechaLimpia = it.sessionDatetime.replace("T", " ")
            binding.tvSubtitle.text = "${it.cinema?.name ?: "Sala"} • $fechaLimpia"

            viewModel.loadSeats(it.id)
        }

        val adapter = SeatsAdapter(emptyList()) { seat ->
            // Cuando se pulsa un asiento:
            viewModel.toggleSeatSelection(seat)

            val total = viewModel.selectedSeats.sumOf { it.price }
            val cantidad = viewModel.selectedSeats.size

            binding.btnConfirm.text = "Comprar $cantidad entradas ($total €)"
        }

        binding.rvSeats.layoutManager = GridLayoutManager(context, 8)
        binding.rvSeats.adapter = adapter

        viewModel.seats.observe(viewLifecycleOwner) { seatList ->
            adapter.updateSeats(seatList)
        }

        binding.btnConfirm.setOnClickListener {
            if (viewModel.selectedSeats.isNotEmpty()) {

                val bundle = Bundle().apply {
                    putSerializable("movie_data", movie)
                    putSerializable("session_data", session)
                    putSerializable("seats_data", ArrayList(viewModel.selectedSeats))
                }

                findNavController().navigate(R.id.action_seatsFragment_to_paymentFragment, bundle)

            } else {
                Toast.makeText(context, "Selecciona al menos un asiento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}