package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscriptoItem

class InscriptosAdapter(
    private var lista: List<InscriptoItem>
) : RecyclerView.Adapter<InscriptosAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtNombreApellido: TextView = view.findViewById(R.id.txtNombreApellido)
        val txtDocumento: TextView = view.findViewById(R.id.txtDocumento)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreo)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inscripto, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.txtNombreApellido.text = "${item.nombre} ${item.apellido}"
        holder.txtDocumento.text = "Doc: ${item.documento}"
        holder.txtCorreo.text = item.correo
        holder.txtFecha.text = item.fechaCreacion
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<InscriptoItem>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
