package com.example.sportbookingapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbookingapp.R
import com.example.sportbookingapp.backend_classes.Reservation
import org.w3c.dom.Text

class ReservationStatusAdapter(private val reservationList: ArrayList<Reservation>) : RecyclerView.Adapter<ReservationStatusAdapter.MyViewHolder>(){

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = reservationList[position]

        holder.date.text = currentItem.date
        holder.endingHour.text = currentItem.endingHour.toString()
        holder.fieldId.text = currentItem.fieldId
        holder.price.text = currentItem.price.toString()
        holder.startingHour.text = currentItem.startingHour.toString()
        holder.status.text = currentItem.status
    }


    override fun getItemCount(): Int {
        return reservationList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.tvDate)
        val endingHour: TextView = itemView.findViewById(R.id.tvEndingHour)
        val fieldId : TextView = itemView.findViewById(R.id.tvFieldId)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        val startingHour : TextView = itemView.findViewById(R.id.tvStartingHour)
        val status : TextView = itemView.findViewById(R.id.tvStatus)

    }
}