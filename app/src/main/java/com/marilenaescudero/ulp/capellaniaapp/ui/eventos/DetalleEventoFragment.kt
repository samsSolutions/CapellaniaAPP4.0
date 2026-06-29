package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentDetalleEventoBinding
import java.io.File

class DetalleEventoFragment : Fragment() {

    private var _binding: FragmentDetalleEventoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventosViewModel
    private var idEvento: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleEventoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(
            requireActivity(),
            EventosViewModelFactory(requireContext())
        )[EventosViewModel::class.java]

        idEvento = arguments?.getInt("idEvento") ?: -1
        if (idEvento == -1) {
            Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        configurarBotones()
        observarViewModel()

        viewModel.cargarDetalle(idEvento)
        verificarInscripcion(idEvento)
    }

    private fun configurarBotones() {

        binding.btnInscribirse.setOnClickListener {
            Toast.makeText(requireContext(), "Función de inscripción aún no implementada", Toast.LENGTH_SHORT).show()
        }

        if (usuarioEsCapellanOAdmin()) {
            binding.btnEditar.visibility = View.VISIBLE
            binding.btnEliminar.visibility = View.VISIBLE
        } else {
            binding.btnEditar.visibility = View.GONE
            binding.btnEliminar.visibility = View.GONE
        }

        binding.btnEditar.setOnClickListener {
            val bundle = Bundle().apply { putInt("idEvento", idEvento) }
            findNavController().navigate(R.id.action_detalleEvento_to_editarContenidoEventos, bundle)
        }

        binding.btnEliminar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar evento")
                .setMessage("¿Seguro que deseas eliminar este evento?")
                .setPositiveButton("Sí") { _, _ ->
                    viewModel.eliminarEvento(idEvento) {
                        Toast.makeText(requireContext(), "Evento eliminado", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun usuarioEsCapellanOAdmin(): Boolean {
        val prefs = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val rol = prefs.getString("rol", "")?.lowercase() ?: ""
        return rol == "capellan" || rol == "administrador"
    }

    private fun observarViewModel() {

        viewModel.detalle.observe(viewLifecycleOwner) { evento ->
            binding.txtNombre.text = evento.nombre
            binding.txtTipo.text = "Tipo: ${evento.tipo}"
            binding.txtFecha.text = "Fecha: ${evento.fecha.substring(0, 10)}"
            binding.txtDireccion.text = "Dirección: ${evento.direccion}"
            binding.txtPublico.text = "Público: ${evento.publicoObjetivo}"
            binding.txtDescripcion.text = evento.descripcion
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

    private fun verificarInscripcion(idEvento: Int) {

        if (usuarioEsCapellanOAdmin()) {
            binding.btnInscribirse.visibility = View.GONE
            return
        }

        viewModel.obtenerInscripcion(idEvento) { inscripcion ->

            if (inscripcion != null) {
                // YA ESTÁ INSCRIPTO
                binding.btnInscribirse.text = "Ver ticket"

                binding.btnInscribirse.setOnClickListener {
                    viewModel.obtenerTicket(inscripcion.idInscripcion) { ruta ->
                        if (ruta != null) abrirPdf(ruta)
                        else Toast.makeText(requireContext(), "No se pudo abrir el ticket", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {

                binding.btnInscribirse.text = "Inscribirme"
                binding.btnInscribirse.setOnClickListener {
                    abrirFormularioInscripcion(idEvento)
                }
            }
        }
    }

    private fun abrirFormularioInscripcion(idEvento: Int) {
        val bottomSheet = InscripcionEventoBottomSheet(idEvento)
        bottomSheet.onDismissCallback = {
            verificarInscripcion(idEvento)
        }

        bottomSheet.show(parentFragmentManager, "InscripcionEvento")
    }

    private fun abrirPdf(ruta: String) {
        val archivo = File(ruta)

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            archivo
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
