package com.app.efficientworkapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private var itemList: MutableList<Item>, private val onItemLongClick: (Item) -> Unit) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Aqui dentro van todas las declaraciones de nuevas variables
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // Se resalta que se debe usar itemView como la vista
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewprueba) // Por ejemplo, ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proyectos, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textViewTitle.text = currentItem.title
        holder.textViewDescription.text = currentItem.description

        val isExpanded = currentItem.isExpanded // isExpanded sirve para verificar estados de los objetos
        holder.textViewDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE
        val isVisible = currentItem.isExpanded // Uso de isExpanded
        holder.imageView.visibility = if (isVisible) View.VISIBLE else View.GONE // Aplicacion
        holder.itemView.setOnClickListener {
            currentItem.isExpanded = !currentItem.isExpanded // Aqui se cambia el estado del objeto actual
            notifyItemChanged(position) // Aqui se notifica el cambio del estado del objeto
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(currentItem)
            true
        }

    }

    override fun getItemCount() = itemList.size

    // Aqui se agrega el metodo para actualizar la lista
    fun updateList(newItems: List<Item>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }

}