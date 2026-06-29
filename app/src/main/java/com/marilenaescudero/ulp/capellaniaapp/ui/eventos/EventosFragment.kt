package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentEventosBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem


class EventosFragment : Fragment() {

    private var _binding: FragmentEventosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventosAdapter
    private lateinit var viewModel: EventosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val menu = toolbar.menu
        val esCapellanOAdmin = usuarioEsCapellanOAdmin()
        menu.findItem(R.id.action_crear_evento)?.isVisible = esCapellanOAdmin
        menu.findItem(R.id.action_mis_creados)?.isVisible = esCapellanOAdmin

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.action_inscripciones -> {
                    findNavController().navigate(R.id.action_eventos_to_inscripcionesEventos)
                    true
                }

                R.id.action_mis_creados -> {
                    findNavController().navigate(R.id.misEventosCreadosFragment)
                    true
                }

                R.id.action_crear_evento -> {
                    mostrarBottomSheetTipoEvento()
                    true
                }

                else -> false
            }
        }

        viewModel = EventosViewModel(requireContext())
        configurarRecycler()
        observarViewModel()

        viewModel.cargarEventosDisponibles()
        viewModel.cargarMisInscripciones()
    }


    private fun mostrarBottomSheetTipoEvento() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_tipo_evento, null)
        dialog.setContentView(view)

        view.findViewById<LinearLayout>(R.id.opcionCapellanes).setOnClickListener {
            abrirFormularioCrearEvento("Capellanes")
            dialog.dismiss()
        }

        view.findViewById<LinearLayout>(R.id.opcionCongresos).setOnClickListener {
            abrirFormularioCrearEvento("Congresos")
            dialog.dismiss()
        }

        view.findViewById<LinearLayout>(R.id.opcionTalleres).setOnClickListener {
            abrirFormularioCrearEvento("Talleres")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun abrirFormularioCrearEvento(tipo: String) {
        val bottom = AgregarContenidoEventos(tipo) { dto ->
            viewModel.crearEvento(dto) {
                Toast.makeText(requireContext(), "Evento creado", Toast.LENGTH_SHORT).show()
                viewModel.cargarEventosDisponibles()
            }
        }
        bottom.show(parentFragmentManager, "crearEvento")
    }

    private fun usuarioEsCapellanOAdmin(): Boolean {
        val prefs = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val rol = prefs.getString("rol", "") ?: ""
        return rol.equals("Capellan", ignoreCase = true) ||
                rol.equals("Administrador", ignoreCase = true)
    }
    private fun actualizarLista(
        eventos: List<EventoItem>?,
        inscripciones: List<InscripcionEventoItem>?
    ) {
        if (eventos == null || inscripciones == null) return

        val eventosInscripta = inscripciones.map { it.idEvento }

        eventos.forEach { evento ->
            evento.inscripta = eventosInscripta.contains(evento.idEvento)
        }


        adapter.actualizarLista(eventos)
    }


    private fun configurarRecycler() {
        adapter = EventosAdapter(mutableListOf()) { item ->
            val bundle = Bundle().apply { putInt("idEvento", item.idEvento) }
            findNavController().navigate(R.id.action_eventos_to_detalleEvento, bundle)
        }

        binding.recyclerEventos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEventos.adapter = adapter
    }

    private fun observarViewModel() {
        viewModel.lista.observe(viewLifecycleOwner) { eventos ->
            actualizarLista(eventos, viewModel.inscripciones.value)
        }

        viewModel.inscripciones.observe(viewLifecycleOwner) { inscripciones ->
            actualizarLista(viewModel.lista.value, inscripciones)
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

