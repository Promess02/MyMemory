package com.example.mymemory.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemory.ButtonItem
import com.example.mymemory.R
import org.w3c.dom.Text

class ButtonAdapter(private val items: List<ButtonItem>, private val onItemClick: (ButtonItem) -> Unit) : RecyclerView.Adapter<ButtonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: Button = itemView.findViewById(R.id.button)

        fun bind(item: ButtonItem) {
            button.text = item.text
        }
    }
}
