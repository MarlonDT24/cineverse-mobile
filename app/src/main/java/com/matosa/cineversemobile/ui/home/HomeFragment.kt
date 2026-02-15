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
import com.matosa.cineversemobile.databinding.FragmentHomeBinding
import com.matosa.cineversemobile.R


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter(emptyList()) { movie ->
            // Creamos el paquete
            val bundle = Bundle().apply {
                putSerializable("movie_data", movie)
            }

            // Navegamos enviando el paquete
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
        binding.rvMovies.layoutManager = LinearLayoutManager(context)
        binding.rvMovies.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.updateMovies(movies)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.starWarsEvents.observe(viewLifecycleOwner) { events ->
            if (!events.isNullOrEmpty()) {
                binding.layoutStarWars.visibility = View.VISIBLE
                val swAdapter = StarWarsAdapter(events)
                binding.rvStarWarsEvents.adapter = swAdapter
            } else {
                binding.layoutStarWars.visibility = View.GONE
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