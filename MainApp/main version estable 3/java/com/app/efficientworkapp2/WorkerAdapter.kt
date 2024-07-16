package com.app.efficientworkapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkerAdapter(
    private var workerList: MutableList<String>,
    private val onDeleteClick: (String) -> Unit,
    private val showDeleteButton: Boolean
) : RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>() {

    inner class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWorkerName: TextView = itemView.findViewById(R.id.textViewWorkerName)
        val buttonDeleteWorker: Button = itemView.findViewById(R.id.buttonDeleteWorker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worker, parent, false)
        return WorkerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val currentWorker = workerList[position]
        holder.textViewWorkerName.text = currentWorker

        holder.buttonDeleteWorker.visibility = if (showDeleteButton) View.VISIBLE else View.GONE // mostrar/ocultar el botón eliminar dependiendo de la vista

        holder.buttonDeleteWorker.setOnClickListener {
            onDeleteClick(currentWorker)
        }
    }

    override fun getItemCount() = workerList.size

    fun updateList(newWorkers: List<String>) {
        workerList = newWorkers.toMutableList()
        notifyDataSetChanged()
    }
}