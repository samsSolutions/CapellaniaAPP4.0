package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentInscripcionesEventosBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem


class InscripcionesEventosFragment : Fragment() {

    private var _binding: FragmentInscripcionesEventosBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventosViewModel
    private lateinit var adapter: InscripcionesEventosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInscripcionesEventosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = EventosViewModel(requireContext())

        configurarRecycler()
        observarViewModel()

        // Cargar inscripciones del usuario
        viewModel.cargarMisInscripciones()
    }

    private fun configurarRecycler() {
        adapter = InscripcionesEventosAdapter(mutableListOf()) { item ->
            // Navegar al detalle del evento
            val bundle = Bundle().apply {
                putInt("idEvento", item.idEvento)
            }
            findNavController().navigate(
                R.id.action_inscripcionesEventos_to_detalleEvento,
                bundle
            )
        }

        binding.recyclerInscripciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerInscripciones.adapter = adapter
    }

    private fun observarViewModel() {

        viewModel.inscripciones.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }

        viewModel.loading.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

