package com.marilenaescudero.ulp.capellaniaapp.ui.inicio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.R

import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentInicioBinding

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener el rol desde SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        val rol = sharedPref.getString("rol", "")

        // Aplicar visibilidad según el rol
        aplicarVisibilidadPorRol(rol)

        // Navegar a FAMILIA
        binding.itemFamilia.root.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_familia)
        }
        // Navegar a ORACIONES
        binding.itemOraciones.root.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_oraciones)
        }
        // Navegar a eventos
        binding.itemEventos.root.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_eventos)
        }
        // Navegar a eventos
        binding.itemPalabra.root.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_palabra)
        }

    }


    fun aplicarVisibilidadPorRol(rol: String?) {
        when (rol) {

            "administrador" -> {
                binding.itemFamilia.root.visibility = View.VISIBLE
                binding.itemMisCasos.root.visibility = View.VISIBLE
                binding.itemOraciones.root.visibility = View.VISIBLE
                binding.itemPalabra.root.visibility = View.VISIBLE
                binding.itemMensajeFe.root.visibility = View.VISIBLE
                binding.itemConsejeria.root.visibility = View.VISIBLE
                binding.itemEventos.root.visibility = View.VISIBLE
            }

            "capellan" -> {
                binding.itemFamilia.root.visibility = View.VISIBLE
                binding.itemMisCasos.root.visibility = View.VISIBLE
                binding.itemOraciones.root.visibility = View.VISIBLE
                binding.itemPalabra.root.visibility = View.VISIBLE
                binding.itemMensajeFe.root.visibility = View.VISIBLE
                binding.itemConsejeria.root.visibility = View.VISIBLE
                binding.itemEventos.root.visibility = View.VISIBLE
            }

            "personal" -> {
                binding.itemFamilia.root.visibility = View.VISIBLE
                binding.itemMisCasos.root.visibility = View.GONE
                binding.itemOraciones.root.visibility = View.VISIBLE
                binding.itemPalabra.root.visibility = View.VISIBLE
                binding.itemMensajeFe.root.visibility = View.VISIBLE
                binding.itemConsejeria.root.visibility = View.VISIBLE
                binding.itemEventos.root.visibility = View.VISIBLE
            }

            "familiar" -> {
                binding.itemFamilia.root.visibility = View.VISIBLE
                binding.itemMisCasos.root.visibility = View.GONE
                binding.itemOraciones.root.visibility = View.VISIBLE
                binding.itemPalabra.root.visibility = View.VISIBLE
                binding.itemMensajeFe.root.visibility = View.VISIBLE
                binding.itemConsejeria.root.visibility = View.VISIBLE
                binding.itemEventos.root.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
