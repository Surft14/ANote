package com.example.anotes.view_model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anotes.R
import com.example.anotes.databinding.NoteItemBinding
import com.example.anotes.db_notes.Note

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    private val noteList = ArrayList<Note>()
    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = NoteItemBinding.bind(item)
        fun bind(note: Note){
            binding.tvTitle.text = note.title
            binding.tvID.text = note.id.toString()
            binding.tvDate.text = note.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    fun addNote(note: Note){
        noteList.add(note)
        notifyDataSetChanged()
    }


}