package com.example.anotes.view_model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anotes.R
import com.example.anotes.databinding.NoteItemBinding
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.view_model.click_interface.OnNoteClickListener


class NoteAdapter(private val listener: OnNoteClickListener): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    private val noteList = ArrayList<Note>()
    private val selectedItems = mutableSetOf<Note>() // Хранит выделенные заметки
    inner class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = NoteItemBinding.bind(item)
        fun bind(note: Note, listener: OnNoteClickListener){
            Log.d("MyLog", "NoteAdapter: bind")
            binding.tvTitle.text = note.title
            binding.tvID.text = note.id.toString()
            binding.tvDate.text = note.date
            binding.tvCategoryInNote.text = note.category
            
            

            // Установка цвета в зависимости от выделения
            if (selectedItems.contains(note)) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.red_color)
                )
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.my_dark_primary)
                )
            }

            // Привязка кликов
            itemView.setOnClickListener {
                listener.onNoteClick(note)
            }

            itemView.setOnLongClickListener {
                if (selectedItems.contains(note)) {
                    selectedItems.remove(note)
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.my_dark_primary)) // Сброс цвета
                } else {
                    selectedItems.add(note)
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red_color)) // Выделение
                }
                listener.onNoteLongClick(note)
            }

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
        holder.bind(noteList[position], listener)
    }


    fun addNote(note: Note){// Добовляем заметку
        Log.d("MyLog", "NoteAdapter: addNote")
        Log.i("MyLog", "NoteAdapter: addNote: id note-${note.id}")
        noteList.add(note)
        notifyDataSetChanged()
    }

    fun getNoteList():ArrayList<Note>{// Получаем список заметок
        return noteList
    }

    fun addNote(listNote: List<Note>){// Добовляем список заметок
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

    fun removeNotes(notes: List<Note>){// Удалить список заметок
        Log.d("MyLog", "NoteAdapter: removeNotes")
        notes.forEach { note ->
            Log.i("MyLog", "Note to delete: id=${note.id}, title=${note.title}")
            noteList.remove(note)
        }

    }

    fun clearSelection() { // Убрать выбронную заметку
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun updateNotes(notes: List<Note>){// Обновить список заметок
        Log.d("MyLog", "NoteAdapter: updateNotes")
        clearAll()
        clearSelection()
        addNote(notes)
    }

}