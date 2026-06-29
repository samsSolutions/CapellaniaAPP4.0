package com.marilenaescudero.ulp.capellaniaapp.ui.familia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.databinding.EditarContenidoFamiliaBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem


class EditarContenidoFamilia : Fragment() {

    private var _binding: EditarContenidoFamiliaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FamiliaViewModel

    private var itemId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditarContenidoFamiliaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = FamiliaViewModel(requireContext())

        // Obtener ID del argumento
        itemId = arguments?.getInt("id") ?: -1

        if (itemId == -1) {
            Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        observarViewModel()

        // Cargar item desde el ViewModel
        viewModel.cargarItem(itemId)

        binding.btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }

    private fun observarViewModel() {

        // Cargar datos del item
        viewModel.item.observe(viewLifecycleOwner) { item ->
            binding.etTitulo.setText(item.titulo)
            binding.etReflexion.setText(item.reflexion)
            binding.etVersiculo.setText(item.versiculo)
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

    private fun guardarCambios() {
        val titulo = binding.etTitulo.text.toString()
        val reflexion = binding.etReflexion.text.toString()
        val versiculo = binding.etVersiculo.text.toString()

        if (titulo.isBlank() || reflexion.isBlank() || versiculo.isBlank()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = requireContext().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        val idUsuario = sharedPref.getInt("idUsuario", -1)

        val itemActualizado = FamiliaItem(
            id = itemId,
            titulo = titulo,
            reflexion = reflexion,
            versiculo = versiculo,
            categoria = "",
            usuarioId = idUsuario
        )

        viewModel.actualizar(itemId, itemActualizado) {
            Toast.makeText(requireContext(), "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

