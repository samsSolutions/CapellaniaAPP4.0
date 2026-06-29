package com.marilenaescudero.ulp.capellaniaapp.ui.familia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marilenaescudero.ulp.capellaniaapp.databinding.ItemFamiliaCardBinding
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem

class FamiliaAdapter(
    private var lista: MutableList<FamiliaItem>,
    private val rol: String?,
    private val onEditar: (FamiliaItem) -> Unit,
    private val onEliminar: (FamiliaItem) -> Unit
) : RecyclerView.Adapter<FamiliaAdapter.FamiliaViewHolder>() {

    inner class FamiliaViewHolder(val binding: ItemFamiliaCardBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliaViewHolder {
        val binding = ItemFamiliaCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FamiliaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamiliaViewHolder, position: Int) {
        val item = lista[position]

        holder.binding.txtTitulo.text = item.titulo
        holder.binding.txtReflexion.text = item.reflexion
        holder.binding.txtVersiculo.text = item.versiculo

        val esAdminOCapellan = rol == "Administrador" || rol == "Capellán"

        holder.binding.layoutAcciones.visibility =
            if (esAdminOCapellan) View.VISIBLE else View.GONE

        holder.binding.btnEditar.setOnClickListener { onEditar(item) }
        holder.binding.btnEliminar.setOnClickListener { onEliminar(item) }
    }


    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<FamiliaItem>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun agregarItem(item: FamiliaItem) {
        lista.add(item)
        notifyItemInserted(lista.size - 1)
    }

    fun eliminarItem(item: FamiliaItem) {
        val index = lista.indexOf(item)
        if (index != -1) {
            lista.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

