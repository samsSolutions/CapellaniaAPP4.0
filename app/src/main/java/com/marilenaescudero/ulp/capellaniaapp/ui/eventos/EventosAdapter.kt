package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.databinding.ItemEventosCardBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem

class EventosAdapter(
    private var lista: MutableList<EventoItem>,
    private val onClick: (EventoItem) -> Unit
) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(val binding: ItemEventosCardBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventosCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val item = lista[position]

        holder.binding.txtNombre.text = item.nombre
        holder.binding.txtTipo.text = item.tipo
        holder.binding.txtFecha.text = item.fecha
        holder.binding.txtDireccion.text = item.direccion

        //  muestra estado de insceipcion
        if (item.inscripta) {
            holder.binding.txtEstado.text = "Inscripta"
            holder.binding.txtEstado.setTextColor(Color.WHITE)
            holder.binding.txtEstado.setBackgroundColor(Color.parseColor("#43A047")) // verde éxito
        } else {
            holder.binding.txtEstado.text = "Inscribirse"
            holder.binding.txtEstado.setTextColor(Color.WHITE)
            holder.binding.txtEstado.setBackgroundColor(Color.parseColor("#1565C0")) // azul acción
        }

        holder.binding.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<EventoItem>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}

