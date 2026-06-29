package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.AgregarContenidoOracionesBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.CrearOracionDto

class AgregarContenidoOraciones(
    private val idCategoria: Int,
    private val onGuardar: (CrearOracionDto) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: AgregarContenidoOracionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AgregarContenidoOracionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            guardarContenido()
        }
    }

    private fun guardarContenido() {
        val descripcion = binding.etDescripcion.text.toString()

        if (descripcion.isBlank()) {
            Toast.makeText(requireContext(), "Completa la descripción", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaOracion = CrearOracionDto(
            idCategoria = idCategoria,
            descripcion = descripcion
        )

        onGuardar(nuevaOracion)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



