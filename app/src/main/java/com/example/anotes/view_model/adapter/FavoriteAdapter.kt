package com.example.anotes.view_model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anotes.R
import com.example.anotes.databinding.FavoriteItemBinding
import com.example.anotes.datebase.db_favorite.Favorite
import com.example.anotes.view_model.click_interface.OnFavoriteClickListener

class FavoriteAdapter(private val listener: OnFavoriteClickListener): RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    private val favoriteList = ArrayList<Favorite>()
    private val selectedItems = mutableSetOf<Favorite>() // Хранит выделенные заметки
    class FavoriteHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = FavoriteItemBinding.bind(item)
        fun bind(favorite: Favorite, listener: OnFavoriteClickListener){
            Log.d("MyLog", "FavoriteAdapter: bind")

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        Log.d("MyLog", "FavoriteAdapter: onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
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
}