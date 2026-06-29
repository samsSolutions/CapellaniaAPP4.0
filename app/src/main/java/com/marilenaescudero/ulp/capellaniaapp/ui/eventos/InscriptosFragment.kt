package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.R

class InscriptosFragment : Fragment() {

    private lateinit var viewModel: EventosViewModel
    private lateinit var rv: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var adapter: InscriptosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inscriptos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.rvInscriptos)
        progress = view.findViewById(R.id.progressInscriptos)

        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = InscriptosAdapter(emptyList())
        rv.adapter = adapter

        viewModel = ViewModelProvider(
            requireActivity(),
            EventosViewModelFactory(requireContext())
        )[EventosViewModel::class.java]

        val idEvento = requireArguments().getInt("idEvento")
        val txtCantidad = view.findViewById<TextView>(R.id.txtCantidad)

        viewModel.inscriptosEvento.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
            txtCantidad.text = "Inscriptos: ${lista.size}"
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.cargarInscriptos(idEvento)
    }

}
