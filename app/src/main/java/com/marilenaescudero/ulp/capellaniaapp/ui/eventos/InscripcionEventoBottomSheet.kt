package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.BottomsheetInscripcionBinding
import java.io.File

class InscripcionEventoBottomSheet(
    private val idEvento: Int
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetInscripcionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventosViewModel

    var onDismissCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetInscripcionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            EventosViewModelFactory(requireContext())
        )[EventosViewModel::class.java]

        cargarDatosUsuario()
        configurarBotonGuardar()
    }

    private fun cargarDatosUsuario() {
        val prefs = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        binding.edtNombre.setText(prefs.getString("nombre", ""))
        binding.edtApellido.setText(prefs.getString("apellido", ""))
        binding.edtCorreo.setText(prefs.getString("correo", ""))
    }

    private fun configurarBotonGuardar() {
        binding.btnGuardar.setOnClickListener {

            val documento = binding.edtDocumento.text.toString().toIntOrNull()

            if (documento == null || documento < 1000000) {
                Toast.makeText(requireContext(), "Documento inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Confirmar inscripción")
                .setMessage("¿Desea inscribirse al evento?")
                .setPositiveButton("Sí") { _, _ ->
                    viewModel.inscribirse(idEvento, documento) { ok, msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

