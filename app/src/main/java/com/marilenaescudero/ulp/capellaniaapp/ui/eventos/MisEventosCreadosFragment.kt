package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.R
import androidx.navigation.fragment.findNavController


class MisEventosCreadosFragment : Fragment() {

    private lateinit var viewModel: EventosViewModel
    private lateinit var rv: RecyclerView
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mis_eventos_creados, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.rvMisEventos)
        progress = view.findViewById(R.id.progressMisEventos)

        viewModel = ViewModelProvider(
            requireActivity(),
            EventosViewModelFactory(requireContext())
        )[EventosViewModel::class.java]

        rv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.eventosCreados.observe(viewLifecycleOwner) { lista ->
            rv.adapter = MisEventosCreadosAdapter(lista) { evento ->

                val bundle = Bundle().apply {
                    putInt("idEvento", evento.idEvento)
                }

                findNavController().navigate(
                    R.id.action_misEventosCreadosFragment_to_inscriptosFragment,
                    bundle
                )
            }
        }


        viewModel.loading.observe(viewLifecycleOwner) {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.cargarEventosCreados()
    }
}
