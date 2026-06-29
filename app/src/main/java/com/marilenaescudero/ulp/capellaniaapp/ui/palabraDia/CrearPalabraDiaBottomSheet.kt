package com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.BottomsheetCrearPalabraDiaBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.CrearPalabraDiaDto

class CrearPalabraDiaBottomSheet(
    private val onGuardar: (CrearPalabraDiaDto) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetCrearPalabraDiaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetCrearPalabraDiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            val versiculo = binding.txtVersiculo.text.toString().trim()
            val reflexion = binding.txtReflexion.text.toString().trim()
            val multimedia = binding.txtMultimedia.text.toString().trim()

            if (versiculo.isEmpty() || reflexion.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dto = CrearPalabraDiaDto(
                versiculo = versiculo,
                reflexion = reflexion,
                multimedia = multimedia.ifEmpty { null }
            )

            onGuardar(dto)
            dismiss()
        }
    }
}