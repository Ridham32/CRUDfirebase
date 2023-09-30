package com.ridhamsharma.crudfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    var notesList: ArrayList<NotesDataClass>,
    var notesInterface: NotesInterface
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.findViewById<TextView>(R.id.tvTitle)
        var description = view.findViewById<TextView>(R.id.tvDescription)
        var btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerlistview,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.setText(notesList[position].title)
            holder.description.setText(notesList[position].description)
            holder.btnUpdate.setOnClickListener{
                notesInterface.OnUpdate(notesList[position],position)
            }
        holder.btnDelete.setOnClickListener {
            notesInterface.OnDelete(notesList[position], position)
        }
    }


}