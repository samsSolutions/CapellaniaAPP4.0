package com.marilenaescudero.ulp.capellaniaapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentHomeBinding
import com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia.PalabraDiaViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa la palabra del día (DTO simple)
        viewModel.palabra.observe(viewLifecycleOwner) { palabra ->
            binding.txtVersiculoHome.text = palabra?.versiculo ?: "No hay palabra del día disponible."
        }

        // Observa errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        // Cargar palabra del día
        viewModel.cargarPalabraHoy()
    }
}
