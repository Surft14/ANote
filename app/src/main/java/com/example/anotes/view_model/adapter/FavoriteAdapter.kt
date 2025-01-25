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
import com.example.anotes.view_model.click_interface.OnFavoriteClickListener
import com.example.anotes.view_model.click_interface.OnNoteClickListener

class FavoriteAdapter(private val listener: OnNoteClickListener): RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    private val favoriteList = ArrayList<Note>()
    private val selectedItems = mutableSetOf<Note>() // Хранит выделенные заметки
    inner class FavoriteHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = NoteItemBinding.bind(item)
        fun bind(note: Note, listener: OnNoteClickListener){
            Log.d("MyLog", "FavoriteAdapter: bind")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        Log.d("MyLog", "FavoriteAdapter: onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("MyLog", "FavoriteAdapter: getItemCount")
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        Log.d("MyLog", "FavoriteAdapter: onBindViewHolder")
        holder.bind(favoriteList[position], listener)
    }

    fun getFavoriteList(): List<Note>{
        return favoriteList
    }

    fun addFavoriteNote(note: Note){
        Log.d("MyLog", "FavoriteAdapter: addFavoriteNote()")
        Log.i("MyLog", "FavoriteAdapter: addFavoriteNote: id note-${note.id}, name category-${note.category}")
        favoriteList.add(note)
        notifyDataSetChanged()
    }

    fun addFavoriteList(notes: List<Note>){
        Log.d("MyLog", "FavoriteAdapter: addFavoriteList(List)")
        for (note in notes){
            addFavoriteNote(note)
        }
        notifyDataSetChanged()
    }

    fun clearAll(){
        Log.d("MyLog", "FavoriteAdapter: clearAll")
        favoriteList.clear()
        notifyDataSetChanged()
    }

    fun clearSelected(){
        Log.d("MyLog", "FavoriteAdapter: clearSelected")
        selectedItems.clear()
        notifyDataSetChanged()
    }

}