package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.AgregarContenidoEventosBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.CrearEventoDto
import java.time.LocalDate
import java.util.Calendar

class AgregarContenidoEventos(
    private val tipoPredefinido: String?,
    private val onGuardar: (CrearEventoDto) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: AgregarContenidoEventosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AgregarContenidoEventosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-cargar tipo si viene desde el selector
        tipoPredefinido?.let {
            binding.etTipo.setText(it)
        }

        // Calendario para seleccionar fecha
        binding.etFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val año = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.etFecha.setText(fechaSeleccionada)
            }, año, mes, dia)

            // Bloquear fechas pasadas
            datePicker.datePicker.minDate = System.currentTimeMillis()

            datePicker.show()
        }

        // Guardar evento
        binding.btnGuardar.setOnClickListener {
            guardarEvento()
        }
    }

    private fun guardarEvento() {

        val nombre = binding.etNombre.text.toString().trim()
        val tipo = binding.etTipo.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val fechaTexto = binding.etFecha.text.toString().trim()
        val publico = binding.etPublico.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val imagen = binding.etImagen.text.toString().trim()
        val cupoTexto = binding.etCupo.text.toString().trim()

        // Validación
        if (nombre.isEmpty() || tipo.isEmpty() || direccion.isEmpty() ||
            fechaTexto.isEmpty() || publico.isEmpty() || descripcion.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir fecha a ISO
        val fechaISO = try {
            LocalDate.parse(fechaTexto).atStartOfDay().toString()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Formato de fecha inválido (usa YYYY-MM-DD)", Toast.LENGTH_SHORT).show()
            return
        }

        val propio = binding.cbPropio.isChecked
        val cupo = cupoTexto.toIntOrNull()

        val dto = CrearEventoDto(
            fecha = fechaISO,
            tipo = tipo,
            nombre = nombre,
            direccion = direccion,
            publicoObjetivo = publico,
            descripcion = descripcion,
            imagen = imagen,
            propio = propio,
            cupo = cupo
        )

        onGuardar(dto)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
