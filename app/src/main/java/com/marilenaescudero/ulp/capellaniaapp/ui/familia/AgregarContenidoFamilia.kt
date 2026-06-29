package com.marilenaescudero.ulp.capellaniaapp.ui.familia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.AgregarContenidoFamiliaBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem


class AgregarContenidoFamilia(
    private val categoria: String,
    private val onGuardar: (FamiliaItem) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: AgregarContenidoFamiliaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AgregarContenidoFamiliaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            guardarContenido()
        }
    }

    private fun guardarContenido() {
        val titulo = binding.etTitulo.text.toString()
        val reflexion = binding.etReflexion.text.toString()
        val versiculo = binding.etVersiculo.text.toString()

        if (titulo.isBlank() || reflexion.isBlank() || versiculo.isBlank()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = requireContext().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        val idUsuario = sharedPref.getInt("idUsuario", -1)

        val nuevoItem = FamiliaItem(
            id = 0,
            titulo = titulo,
            reflexion = reflexion,
            versiculo = versiculo,
            categoria = categoria,
            usuarioId = idUsuario
        )

        onGuardar(nuevoItem)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

