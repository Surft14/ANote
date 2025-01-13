package com.example.anotes.view_model

import android.util.Log
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
            Log.d("MyLog", "NoteAdapter: onCreateViewHolder")
            binding.tvTitle.text = note.title
            binding.tvID.text = note.id.toString()
            binding.tvDate.text = note.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        Log.d("MyLog", "NoteAdapter: onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("MyLog", "NoteAdapter: getItemCount")
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        Log.d("MyLog", "NoteAdapter: onBindViewHolder")
        holder.bind(noteList[position])
    }

    fun addNote(note: Note){
        Log.d("MyLog", "NoteAdapter: addNote")
        Log.i("MyLog", "NoteAdapter: addNote: id note-${note.id}")
        noteList.add(note)
        notifyDataSetChanged()
    }

    fun getNoteList():ArrayList<Note>{
        return noteList
    }

    fun addNote(listNote: List<Note>){
        Log.d("MyLog", "NoteAdapter: addNote(List)")
        for (note in listNote){
            Log.i("MyLog", "NoteAdapter: addNote(List): id note-${note.id}")
            noteList.add(note)
        }
        notifyDataSetChanged()
    }

    fun clearAll() {
        Log.d("MyLog", "NoteAdapter: clearAll")
        noteList.clear() // Очистить список
        notifyDataSetChanged() // Сообщить адаптеру об изменениях
    }
}