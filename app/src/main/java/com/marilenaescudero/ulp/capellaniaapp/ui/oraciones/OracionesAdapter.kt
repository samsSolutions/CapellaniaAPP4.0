package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.databinding.ItemOracionesCardBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.OracionDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OracionesAdapter(
    private var lista: MutableList<OracionDto>,
    private val rol: String?,
    private val onEditar: (OracionDto) -> Unit,
    private val onEliminar: (OracionDto) -> Unit
) : RecyclerView.Adapter<OracionesAdapter.OracionViewHolder>() {

    inner class OracionViewHolder(val binding: ItemOracionesCardBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OracionViewHolder {
        val binding = ItemOracionesCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OracionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OracionViewHolder, position: Int) {
        val item = lista[position]

        // CAMPOS REALES DEL DTO
        holder.binding.txtDescripcion.text = item.descripcion
        holder.binding.txtCapellan.text = "Capellán: ${item.capellanNombre}"
        // FORMATEAR FECHA (solo día, sin hora)
        val formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val fechaSolo = try {
            LocalDateTime.parse(item.fecha, formatoEntrada).format(formatoSalida)
        } catch (e: Exception) {
            item.fecha // fallback si algo falla
        }

        holder.binding.txtFecha.text = "Fecha: $fechaSolo"


        // PERMISOS
        val esAdminOCapellan = rol == "Administrador" || rol == "Capellán"
        holder.binding.layoutAcciones.visibility =
            if (esAdminOCapellan) View.VISIBLE else View.GONE

        // EVENTOS
        holder.binding.btnEditar.setOnClickListener { onEditar(item) }
        holder.binding.btnEliminar.setOnClickListener { onEliminar(item) }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<OracionDto>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun agregarItem(item: OracionDto) {
        lista.add(item)
        notifyItemInserted(lista.size - 1)
    }

    fun eliminarItem(item: OracionDto) {
        val index = lista.indexOf(item)
        if (index != -1) {
            lista.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

