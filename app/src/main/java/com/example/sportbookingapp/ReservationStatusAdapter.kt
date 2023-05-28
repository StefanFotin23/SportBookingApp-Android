package com.example.sportbookingapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbookingapp.R
import com.example.sportbookingapp.backend_classes.Reservation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ReservationStatusAdapter(private val reservationList: ArrayList<Reservation>) :
    RecyclerView.Adapter<ReservationStatusAdapter.MyViewHolder>() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = reservationList[position]

        holder.date.text = currentItem.getDate()
        holder.endingHour.text = currentItem.getEndingHour().toString()
     //   holder.fieldId.text = currentItem.getFieldId()
        holder.price.text = "${currentItem.getPrice()} lei"
        holder.startingHour.text = currentItem.getStartingHour().toString()
        holder.status.text = currentItem.getStatus()
        holder.sportCategory.text = currentItem.getSportField()?.getSportCategory() ?: "Reservation"
        if (currentItem.getStatus() == "cancelled") {
            holder.status.setTextColor(Color.RED)
        } else if (currentItem.getStatus() == "pending") {
            holder.status.setTextColor(Color.parseColor("#FFA500"))
        } else if (currentItem.getStatus() == "active") {
            holder.status.setTextColor(Color.parseColor("#66F9B0"))
        }

        if (currentItem.getSportField() == null) {
            Picasso.get()
                .load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFile%3ANo_image_available.svg&psig=AOvVaw39UB9uSha9F4bJIc_PBnAK&ust=1685375670563000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCKjsjeOvmP8CFQAAAAAdAAAAABAE")
                .error(R.drawable.sports_field_background) // Replace with your error placeholder image
                .into(holder.sportImage, object : Callback {
                    override fun onSuccess() {
                        // Image loaded successfully
                    }

                    override fun onError(e: Exception?) {
                        Log.e(
                            ContentValues.TAG,
                            "Error loading image from URL: ${
                                currentItem.getSportField()?.getImageUrl()
                            }",
                            e
                        )
                    }
                })
        } else {
            Picasso.get()
                .load(currentItem.getSportField()?.getImageUrl())
                .error(R.drawable.sports_field_background) // Replace with your error placeholder image
                .into(holder.sportImage, object : Callback {
                    override fun onSuccess() {
                        // Image loaded successfully
                    }

                    override fun onError(e: Exception?) {
                        Log.e(
                            ContentValues.TAG,
                            "Error loading image from URL: ${
                                currentItem.getSportField()?.getImageUrl()
                            }",
                            e
                        )
                    }
                })
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val endingHour: TextView = itemView.findViewById(R.id.tvEndingHour)
     //   val fieldId: TextView = itemView.findViewById(R.id.tvFieldId)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val startingHour: TextView = itemView.findViewById(R.id.tvStartingHour)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val sportImage: ImageView = itemView.findViewById(R.id.title_image)
        val sportCategory: TextView = itemView.findViewById(R.id.tvHeading)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }
}