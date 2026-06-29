package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.databinding.ItemInscripcionEventoBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem

class InscripcionesEventosAdapter(
    listaInicial: List<InscripcionEventoItem>,
    private val onClick: (InscripcionEventoItem) -> Unit
) : RecyclerView.Adapter<InscripcionesEventosAdapter.ViewHolder>() {

    private val lista = mutableListOf<InscripcionEventoItem>()

    init {
        lista.addAll(listaInicial)
    }

    inner class ViewHolder(val binding: ItemInscripcionEventoBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInscripcionEventoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.binding.txtNombre.text = item.nombre
        holder.binding.txtTipo.text = item.tipo

        val fecha = item.fecha.substring(0, 10)
        holder.binding.txtFecha.text = "Fecha: $fecha"

        holder.binding.txtDireccion.text = item.direccion

        holder.binding.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<InscripcionEventoItem>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
