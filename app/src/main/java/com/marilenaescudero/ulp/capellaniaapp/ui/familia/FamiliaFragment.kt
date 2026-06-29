package com.marilenaescudero.ulp.capellaniaapp.ui.familia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentFamiliaBinding

class FamiliaFragment : Fragment() {

    private var _binding: FragmentFamiliaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamiliaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardEsposo.setOnClickListener {
            findNavController().navigate(R.id.action_familia_to_esposo)
        }

        binding.cardHijos.setOnClickListener {
            findNavController().navigate(R.id.action_familia_to_hijos)
        }

        binding.cardHogar.setOnClickListener {
            findNavController().navigate(R.id.action_familia_to_hogar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

