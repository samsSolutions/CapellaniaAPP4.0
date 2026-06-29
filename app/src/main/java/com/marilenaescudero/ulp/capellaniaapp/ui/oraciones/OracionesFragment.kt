package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentOracionesBinding

class OracionesFragment : Fragment() {

    private var _binding: FragmentOracionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOracionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardInicioServicio.setOnClickListener {
            findNavController().navigate(R.id.action_oraciones_to_iniciarServicio)
        }

        binding.cardProteccionPatrullaje.setOnClickListener {
            findNavController().navigate(R.id.action_oraciones_to_proteccionPatrullaje)
        }

        binding.cardMomentosAngustia.setOnClickListener {
            findNavController().navigate(R.id.action_oraciones_to_momentosAngustia)
        }

        binding.cardCompanerosCaidos.setOnClickListener {
            findNavController().navigate(R.id.action_oraciones_to_companerosCaidos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

