package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.R
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem

class MisEventosCreadosAdapter(
    private val lista: List<EventoItem>,
    private val onVerInscriptos: (EventoItem) -> Unit
) : RecyclerView.Adapter<MisEventosCreadosAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtCupo: TextView = view.findViewById(R.id.txtCupo)
        val btnVerInscriptos: Button = view.findViewById(R.id.btnVerInscriptos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento_creado, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.txtNombre.text = item.nombre
        holder.txtFecha.text = item.fecha
        holder.txtDireccion.text = item.direccion
        holder.txtCupo.text = "Cupo: ${item.cupo}"

        holder.btnVerInscriptos.setOnClickListener {
            onVerInscriptos(item)
        }
    }

    override fun getItemCount() = lista.size
}
