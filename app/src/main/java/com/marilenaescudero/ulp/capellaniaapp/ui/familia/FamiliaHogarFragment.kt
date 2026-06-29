package com.marilenaescudero.ulp.capellaniaapp.ui.familia

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
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentFamiliaHogarBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem


class FamiliaHogarFragment : Fragment() {

    private var _binding: FragmentFamiliaHogarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FamiliaAdapter
    private lateinit var rol: String
    private lateinit var viewModel: FamiliaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamiliaHogarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = FamiliaViewModel(requireContext())

        // Rol del usuario
        val sharedPref = requireActivity()
            .getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        rol = sharedPref.getString("rol", "") ?: ""

        configurarRecycler()
        aplicarPermisos()
        observarViewModel()

        // Primera carga
        viewModel.cargarCategoria("hogar")
        binding.btnCargarMas.setOnClickListener {
            viewModel.cargarCategoria("hogar")
        }
    }

    private fun configurarRecycler() {
        adapter = FamiliaAdapter(
            lista = mutableListOf(),
            rol = rol,
            onEditar = { item -> navegarEditar(item) },
            onEliminar = { item -> eliminarItem(item) }
        )

        binding.recyclerHogar.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHogar.adapter = adapter
    }

    private fun aplicarPermisos() {
        val esAdminOCapellan = rol == "Administrador" || rol == "Capellán"

        binding.btnAgregar.visibility = if (esAdminOCapellan) View.VISIBLE else View.GONE
        binding.btnAgregar.setOnClickListener {
            AgregarContenidoFamilia("hogar") { nuevoItem ->
                viewModel.crear(nuevoItem) { creado ->
                    viewModel.reiniciarPaginacion()
                    viewModel.cargarCategoria("hogar")
                }
            }.show(parentFragmentManager, "AgregarContenido")
        }
    }

    private fun observarViewModel() {

        // Lista de contenidos
        viewModel.lista.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }

        // Loading
        viewModel.loading.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navegarEditar(item: FamiliaItem) {
        val bundle = Bundle().apply { putInt("id", item.id) }
        findNavController().navigate(R.id.action_hogar_to_editarContenido, bundle)
    }

    private fun eliminarItem(item: FamiliaItem) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás segura de que deseas eliminar este contenido?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminar(item.id) {
                    adapter.eliminarItem(item)
                    Toast.makeText(requireContext(), "Eliminado Correctamente", Toast.LENGTH_SHORT).show()
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

