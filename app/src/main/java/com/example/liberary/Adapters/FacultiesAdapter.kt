package com.example.liberary.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.liberary.Faculty
import com.example.liberary.R

class FacultiesAdapter(private var itemList: ArrayList<Faculty>, private val onItemClicked: (position: Int) -> Unit): RecyclerView.Adapter<FacultiesAdapter.MyViewHolder>()  {
    class MyViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit):RecyclerView.ViewHolder(view), View.OnClickListener{
        var facultyName: TextView = view.findViewById(R.id.faculty_name)
        var facultyImage: ImageView = view.findViewById(R.id.faculty_image)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultiesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.faculty_rv_item, parent, false)
        return FacultiesAdapter.MyViewHolder(itemView, onItemClicked)
    }
    override fun onBindViewHolder(holder: FacultiesAdapter.MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.facultyName.text = item.FacultyName
        holder.facultyImage.setImageResource(item.image)

    }
    override fun getItemCount(): Int {
        return  itemList.size
    }
}