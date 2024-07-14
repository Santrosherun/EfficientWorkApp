package com.app.efficientworkapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(
    private var activityList: MutableList<Activity>,
    private val onItemClick: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewActivityTitle: TextView = itemView.findViewById(R.id.textViewActivityTitle)
        val textViewActivityDescription: EditText = itemView.findViewById(R.id.textViewActivityDescription)
        val textViewActivityImage: ImageView = itemView.findViewById(R.id.imageViewprueba)
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
    }

    override fun getItemCount() = activityList.size

    fun updateList(newActivities: List<Activity>) {
        activityList = newActivities.toMutableList()
        notifyDataSetChanged()
    }
}