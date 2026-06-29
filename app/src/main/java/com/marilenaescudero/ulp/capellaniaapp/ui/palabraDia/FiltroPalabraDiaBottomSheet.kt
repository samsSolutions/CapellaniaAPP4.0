package com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marilenaescudero.ulp.capellaniaapp.databinding.BottomsheetFiltroPalabraDiaBinding

class FiltroPalabraDiaBottomSheet(
    private val onAplicarFiltros: (String?, String?, String?) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetFiltroPalabraDiaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetFiltroPalabraDiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAplicar.setOnClickListener {
            val fecha = binding.txtFecha.text.toString().trim().ifEmpty { null }
            val estado = binding.spinnerEstado.selectedItem.toString().trim().ifEmpty { null }
            val capellan = binding.txtCapellan.text.toString().trim().ifEmpty { null }

            onAplicarFiltros(fecha, estado, capellan)
            dismiss()
        }
    }
}
