package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.databinding.EditarContenidoOracionesBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.EditarOracionDto

class EditarContenidoOraciones : Fragment() {

    private var _binding: EditarContenidoOracionesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OracionesViewModel
    private var idOracion: Int = -1
    private var idCategoria: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditarContenidoOracionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = OracionesViewModel(requireContext())

        idOracion = arguments?.getInt("id") ?: -1

        if (idOracion == -1) {
            Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        observarViewModel()

        // Cargar oración desde el ViewModel
        viewModel.cargarItem(idOracion)

        binding.btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }

    private fun observarViewModel() {

        viewModel.item.observe(viewLifecycleOwner) { oracion ->
            idCategoria = oracion.idCategoria
            binding.etDescripcion.setText(oracion.descripcion)
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

    private fun guardarCambios() {
        val descripcion = binding.etDescripcion.text.toString()

        if (descripcion.isBlank()) {
            Toast.makeText(requireContext(), "Completa la descripción", Toast.LENGTH_SHORT).show()
            return
        }

        val dto = EditarOracionDto(
            descripcion = descripcion
        )

        viewModel.editar(idOracion, dto) {
            Toast.makeText(requireContext(), "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
