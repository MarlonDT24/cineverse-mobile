package com.matosa.cineversemobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.matosa.cineversemobile.databinding.FragmentSessionsBinding
import com.matosa.cineversemobile.model.Movie
import com.matosa.cineversemobile.R

class SessionsFragment : Fragment() {

    private var _binding: FragmentSessionsBinding? = null
    private val binding get() = _binding!!
    private var movie: Movie? = null

    private val viewModel: SessionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getSerializable("movie_data") as Movie?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie?.let {
            binding.tvMovieTitle.text = "Horarios para: ${it.title}"
            // Pedimos las sesiones al servidor
            viewModel.loadSessions(it.id)
        }

        val adapter = SessionsAdapter(emptyList()) { session ->
            val bundle = Bundle().apply {
                putSerializable("session_data", session)
                putSerializable("movie_data", movie)
            }
            findNavController().navigate(R.id.action_sessionsFragment_to_seatsFragment, bundle)
        }
        binding.rvSessions.layoutManager = LinearLayoutManager(context)
        binding.rvSessions.adapter = adapter

        viewModel.sessions.observe(viewLifecycleOwner) { sessionList ->
            if (sessionList.isEmpty()) {
                Toast.makeText(context, "No hay sesiones para esta película", Toast.LENGTH_LONG).show()
            } else {
                adapter.updateSessions(sessionList)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}