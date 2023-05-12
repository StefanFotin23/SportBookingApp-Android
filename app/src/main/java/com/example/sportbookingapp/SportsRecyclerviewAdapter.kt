package com.example.sportbookingapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SportsRecyclerviewAdapter(private val sportsList: ArrayList<Sports>):
    RecyclerView.Adapter<SportsRecyclerviewAdapter.SportsRecyclerviewViewHolder>() {

    private var selectedPosition = -1 // no image is selected

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportsRecyclerviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sports_recycler_view_item, parent, false)
        return SportsRecyclerviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SportsRecyclerviewViewHolder,
                                  @SuppressLint("RecyclerView") position: Int) {
        val currentItem = sportsList[position]
        holder.sportImage.setImageResource(currentItem.sportImage)
        holder.sportName.text = currentItem.sportName

        if (position == selectedPosition) {
            holder.itemView.findViewById<RelativeLayout>(R.id.sportsRecyclerviewLayout)
                .setBackgroundColor(Color.parseColor("#66F9B0"))
        } else {
            holder.itemView.findViewById<RelativeLayout>(R.id.sportsRecyclerviewLayout)
                .setBackgroundColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                // Deselect the previously selected item (if any)
                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition)
                }
                // Select the clicked item
                selectedPosition = position
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return sportsList.size
    }

    class SportsRecyclerviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sportImage: ImageView = itemView.findViewById(R.id.sportFieldImage)
        val sportName: TextView = itemView.findViewById(R.id.sportFieldText)
    }
}