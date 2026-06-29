package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentIniciarServicioBinding
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentMomentosAngustiaBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.OracionDto


class MomentosAngustiaFragment : Fragment() {
    private var _binding: FragmentMomentosAngustiaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OracionesAdapter
    private lateinit var rol: String
    private lateinit var viewModel: OracionesViewModel

    private val idCategoria = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomentosAngustiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = OracionesViewModel(requireContext())

        val sharedPref = requireActivity()
            .getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        rol = sharedPref.getString("rol", "") ?: ""

        configurarRecycler()
        aplicarPermisos()
        observarViewModel()

        // Primera carga
        viewModel.cargarCategoria(idCategoria)

        binding.btnCargarMas.setOnClickListener {
            viewModel.cargarCategoria(idCategoria)
        }
    }

    private fun configurarRecycler() {
        adapter = OracionesAdapter(
            lista = mutableListOf(),
            rol = rol,
            onEditar = { item -> navegarEditar(item) },
            onEliminar = { item -> eliminarItem(item) }
        )

        binding.recyclerMomentosAngustia.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMomentosAngustia.adapter = adapter
    }

    private fun aplicarPermisos() {
        val esAdminOCapellan = rol == "Administrador" || rol == "Capellán"

        binding.btnAgregar.visibility = if (esAdminOCapellan) View.VISIBLE else View.GONE

        binding.btnAgregar.setOnClickListener {
            AgregarContenidoOraciones(idCategoria) { nuevaOracion ->
                viewModel.crear(nuevaOracion) {
                    // Recargar desde página 1
                    viewModel.reiniciarPaginacion()
                    viewModel.cargarCategoria(idCategoria)
                }
            }.show(parentFragmentManager, "AgregarContenidoOraciones")
        }
    }

    private fun observarViewModel() {
        viewModel.lista.observe(viewLifecycleOwner) { lista ->
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

    private fun navegarEditar(item: OracionDto) {
        val bundle = Bundle().apply { putInt("id", item.idOracion) }
        findNavController().navigate(R.id.action_momentosAngustia_to_editarContenidoOraciones, bundle)
    }

    private fun eliminarItem(item: OracionDto) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Deseas eliminar esta oración?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminar(item.idOracion) {
                    adapter.eliminarItem(item)
                    Toast.makeText(requireContext(), "Eliminada correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
