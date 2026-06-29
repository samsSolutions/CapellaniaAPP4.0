package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.EditarContenidoEventosBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.ActualizarEventoDto
import java.time.LocalDate
import androidx.navigation.fragment.findNavController
import java.util.Calendar


class EditarContenidoEventos : Fragment() {

    private var _binding: EditarContenidoEventosBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventosViewModel
    private var idEvento: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditarContenidoEventosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = EventosViewModel(requireContext())

        idEvento = arguments?.getInt("idEvento") ?: -1

        if (idEvento == -1) {
            Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        observarViewModel()
        viewModel.cargarDetalle(idEvento)

        // Calendario para editar fecha
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

        binding.btnActualizar.setOnClickListener {
            actualizarEvento()
        }
    }

    private fun observarViewModel() {

        viewModel.detalle.observe(viewLifecycleOwner) { evento ->
            binding.etNombre.setText(evento.nombre)
            binding.etTipo.setText(evento.tipo)
            binding.etDireccion.setText(evento.direccion)
            binding.etFecha.setText(evento.fecha.substring(0, 10))
            binding.etPublico.setText(evento.publicoObjetivo)
            binding.etDescripcion.setText(evento.descripcion)
            binding.etImagen.setText(evento.imagen)
            binding.cbPropio.isChecked = evento.propio
            binding.etCupo.setText(evento.cupo?.toString() ?: "")
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

    private fun actualizarEvento() {

        val fechaTexto = binding.etFecha.text.toString().trim()

        val fechaISO = try {
            LocalDate.parse(fechaTexto).atStartOfDay().toString()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Formato de fecha inválido (usa YYYY-MM-DD)", Toast.LENGTH_SHORT).show()
            return
        }

        val dto = ActualizarEventoDto(
            fecha = fechaISO,
            tipo = binding.etTipo.text.toString(),
            nombre = binding.etNombre.text.toString(),
            direccion = binding.etDireccion.text.toString(),
            publicoObjetivo = binding.etPublico.text.toString(),
            descripcion = binding.etDescripcion.text.toString(),
            imagen = binding.etImagen.text.toString(),
            propio = binding.cbPropio.isChecked,
            cupo = binding.etCupo.text.toString().toIntOrNull()
        )

        viewModel.actualizarEvento(idEvento, dto) {
            Toast.makeText(requireContext(), "Evento actualizado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
