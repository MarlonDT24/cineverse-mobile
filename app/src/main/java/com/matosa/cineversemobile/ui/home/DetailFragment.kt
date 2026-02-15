package com.matosa.cineversemobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.databinding.FragmentDetailBinding
import com.matosa.cineversemobile.model.Movie

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var movie: Movie? = null

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
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Rellenamos la pantalla con los datos de cada película
        movie?.let { m ->
            binding.tvDetailTitle.text = m.title
            binding.tvDetailDesc.text = m.description ?: "Sin sinopsis disponible."

            val info = "${m.duration ?: 0} min • ${m.genre} • ${m.director}"
            binding.tvDetailInfo.text = info

            // Carga la imagen grande
            Glide.with(this)
                .load(m.posterUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.ivDetailPoster)

            binding.btnBuyTicket.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("movie_data", m)
                }
                findNavController().navigate(R.id.action_detailFragment_to_sessionsFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}