package com.matosa.cineversemobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.databinding.FragmentPaymentBinding
import com.matosa.cineversemobile.model.BookingRequest
import com.matosa.cineversemobile.model.Movie
import com.matosa.cineversemobile.model.Seat
import com.matosa.cineversemobile.model.Session
import com.matosa.cineversemobile.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.ArrayList

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private var movie: Movie? = null
    private var session: Session? = null
    private var selectedSeats: ArrayList<Seat>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getSerializable("movie_data") as Movie?
            session = it.getSerializable("session_data") as Session?
            selectedSeats = it.getSerializable("seats_data") as ArrayList<Seat>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMovieTitle.text = movie?.title ?: "Película Desconocida"

        session?.let {
            val fecha = it.sessionDatetime.replace("T", " ")
            val nombreSala = it.cinema?.name ?: "Sala ${it.cinema?.id ?: "?"}"
            binding.tvSessionInfo.text = "$fecha • $nombreSala"
        }

        val seatNames = selectedSeats?.joinToString(", ") { "${it.row}${it.number}" }
        binding.tvSeatsList.text = "Asientos: $seatNames"

        val total = selectedSeats?.sumOf { it.price } ?: 0.0
        binding.tvTotalPrice.text = "$total €"

        binding.btnPayNow.setOnClickListener {
            realizarReserva(total)
        }
    }

    private fun realizarReserva(total: Double) {
        // Desactivamos el botón para que no pulsen dos veces
        binding.btnPayNow.isEnabled = false
        binding.btnPayNow.text = "Procesando..."

        // Obtenemos los datos necesarios
        val userId = 1
        val sessionId = session?.id ?: 0
        val seatIds = selectedSeats?.map { it.id } ?: emptyList()

        val request = BookingRequest(userId, sessionId, seatIds, total)

        lifecycleScope.launch {
            try {
                // LLAMADA AL SERVIDOR
                val response = RetrofitClient.instance.createBooking(request)

                if (response.isSuccessful) {
                    Toast.makeText(context, "¡Reserva completada! ", Toast.LENGTH_LONG).show()
                    volverAlInicio()
                } else {
                    Toast.makeText(context, "Reserva simulada (Server: ${response.code()})", Toast.LENGTH_SHORT).show()
                    volverAlInicio()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Modo Offline: Reserva simulada correcta", Toast.LENGTH_SHORT).show()
                volverAlInicio()
            } finally {
                binding.btnPayNow.isEnabled = true
                binding.btnPayNow.text = "Realizar Pago"
            }
        }
    }

    private fun volverAlInicio() {
        // Navegamos al Home y borramos todo el historial de pantallas intermedias
        findNavController().navigate(R.id.action_paymentFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}