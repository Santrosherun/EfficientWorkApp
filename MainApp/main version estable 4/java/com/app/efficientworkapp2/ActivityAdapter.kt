package com.app.efficientworkapp2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(
    private var activityList: MutableList<Activity>,
    private val onItemClick: (Activity) -> Unit,
    private val sharedViewModel: SharedViewModel,
    private val parentItem: Item
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewActivityTitle: TextView = itemView.findViewById(R.id.textViewActivityTitle)
        val textViewActivityDescription: TextView = itemView.findViewById(R.id.textViewActivityDescription)
        val textViewActivityImage: ImageView = itemView.findViewById(R.id.imageViewprueba)
        val buttonAddWorker: Button = itemView.findViewById(R.id.buttonAddWorker)
        val recyclerViewWorkers: RecyclerView = itemView.findViewById(R.id.recyclerViewWorkers)

        /*
        fun showAssignWorkersDialog(activity: Activity) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Asignar Trabajadores")

            val workers = sharedViewModel.workerList.value ?: listOf()
            val selectedWorkers = BooleanArray(workers.size)
            builder.setMultiChoiceItems(workers.toTypedArray(), selectedWorkers) { _, which, isChecked ->
                selectedWorkers[which] = isChecked
            }

            builder.setPositiveButton("Asignar") { _, _ ->
                val assignedWorkers = workers.filterIndexed { index, _ -> selectedWorkers[index] }
                activity.workers = assignedWorkers.toMutableList()
                notifyDataSetChanged()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

            builder.show()
        }
        */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentActivity = activityList[position]
        holder.textViewActivityTitle.text = currentActivity.title

        holder.textViewActivityDescription.visibility = if (currentActivity.isExpanded) View.VISIBLE else View.GONE
        holder.textViewActivityImage.visibility = if (currentActivity.isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            onItemClick(currentActivity)
            notifyItemChanged(position)
        }

        holder.buttonAddWorker.setOnClickListener {
            showAssignWorkerDialog(holder.itemView.context, currentActivity, parentItem, position)
        }

        holder.recyclerViewWorkers.layoutManager = LinearLayoutManager(holder.itemView.context)
        val workersAdapter = WorkerAdapter(currentActivity.workers.toMutableList(), { worker ->
        }, showDeleteButton = false)
        holder.recyclerViewWorkers.adapter = workersAdapter

        holder.recyclerViewWorkers.visibility = if (currentActivity.isExpanded) View.VISIBLE else View.GONE
        holder.buttonAddWorker.visibility = if (currentActivity.isExpanded) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = activityList.size

    private fun showAssignWorkerDialog(context: Context, activity: Activity, parentItem: Item, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Asignar Trabajadores")

        val workerList = sharedViewModel.workerList.value ?: mutableListOf()
        val checkedItems = BooleanArray(workerList.size) { i -> activity.workers.contains(workerList[i]) }

        builder.setMultiChoiceItems(workerList.toTypedArray(), checkedItems) { _, which, isChecked ->
            if (isChecked) {
                if (!activity.workers.contains(workerList[which])) {
                    activity.workers.add(workerList[which])
                }
            } else {
                activity.workers.remove(workerList[which])
            }
        }

        builder.setPositiveButton("OK") { dialog, _ ->
            notifyItemChanged(position)
            sharedViewModel.updateActivity(parentItem, activity)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun updateList(newActivities: List<Activity>) {
        activityList = newActivities.toMutableList()
        notifyDataSetChanged()
    }
}