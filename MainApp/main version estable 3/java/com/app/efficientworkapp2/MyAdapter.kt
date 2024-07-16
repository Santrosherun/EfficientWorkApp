package com.app.efficientworkapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.contracts.contract

class MyAdapter(
    private var itemList: MutableList<Item>,
    private val viewModel: SharedViewModel,
    private val onItemLongClick: (Item) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Aqui dentro van todas las declaraciones de nuevas variables
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // Se resalta que se debe usar itemView como la vista
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewprueba) // Por ejemplo, ImageView
        val buttonAddActivity: Button = itemView.findViewById(R.id.buttonAddActivity)
        val recyclerViewActivities: RecyclerView = itemView.findViewById(R.id.recyclerViewActivities)
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
        holder.imageView.visibility = if (isExpanded) View.VISIBLE else View.GONE // Aplicacion
        holder.recyclerViewActivities.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.buttonAddActivity.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded && currentItem.activities.isNotEmpty()) {
            holder.recyclerViewActivities.visibility = View.VISIBLE
        } else {
            holder.recyclerViewActivities.visibility = View.GONE
        }


        holder.recyclerViewActivities.layoutManager = LinearLayoutManager(holder.itemView.context)
        val activityAdapter = ActivityAdapter(currentItem.activities, onItemClick = { activity -> // onItemClick
            activity.isExpanded = !activity.isExpanded
            viewModel.updateItem(currentItem)
        }, sharedViewModel = viewModel, currentItem)
        holder.recyclerViewActivities.adapter = activityAdapter

        holder.buttonAddActivity.setOnClickListener {
            val newActivity = Activity("Nueva Actividad ${currentItem.activities.size + 1}")
            currentItem.activities.add(newActivity)
            activityAdapter.notifyItemInserted(currentItem.activities.size - 1)
            viewModel.updateItem(currentItem)
            holder.recyclerViewActivities.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            currentItem.isExpanded = !currentItem.isExpanded // Aqui se cambia el estado del objeto actual
            viewModel.updateItem(currentItem)
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