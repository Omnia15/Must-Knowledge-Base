package com.example.liberary.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.liberary.R

class PrerequisiteAdapter(private var prerequisites: List<String>): RecyclerView.Adapter<PrerequisiteAdapter.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view),
        View.OnClickListener{
        var itemTextView: TextView = view.findViewById(R.id.prerequistName)
        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.prerequisites_rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemTextView.text = prerequisites[position]
    }

    override fun getItemCount(): Int {
      return prerequisites.size
    }
}