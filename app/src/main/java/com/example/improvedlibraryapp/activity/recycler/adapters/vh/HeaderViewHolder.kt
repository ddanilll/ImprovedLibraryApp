package com.example.improvedlibraryapp.activity.recycler.adapters.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.improvedlibraryapp.R
import com.example.improvedlibraryapp.activity.recycler.Item

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val titleView: TextView = view.findViewById(R.id.sectionTitle)

    fun bind(header: Item.Header) {
        titleView.text = header.title
    }
}