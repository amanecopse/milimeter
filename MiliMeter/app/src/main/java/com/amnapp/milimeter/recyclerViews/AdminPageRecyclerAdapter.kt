package com.amnapp.milimeter.recyclerViews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData

class AdminPageRecyclerAdapter(var subUserlist: MutableList<UserData>): RecyclerView.Adapter<AdminPageRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val userNameTv = itemView.findViewById<TextView>(R.id.userNameTv)
        val militaryIdTv = itemView.findViewById<TextView>(R.id.militaryIdTv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin_page,parent,false) as ViewGroup
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        subUserlist.get(position).let { item ->
            with(holder) {
                userNameTv.text = item.userName
                militaryIdTv.text = item.militaryId.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return subUserlist.size
    }
}