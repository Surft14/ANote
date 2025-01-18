package com.example.anotes.view_model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anotes.R
import com.example.anotes.databinding.NoteItemBinding
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.view_model.click_interface.OnCategoryClickListener
import com.example.anotes.view_model.click_interface.OnNoteClickListener
import kotlin.collections.ArrayList

class CategoryAdapter(private val listener: OnCategoryClickListener): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    private var categoryList = ArrayList<Category>()
    private val selectedItems = mutableSetOf<Category>() // Хранит выделенные заметки
    inner class CategoryHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = NoteItemBinding.bind(item)
        fun bind(category: Category, listener: OnCategoryClickListener){
            Log.d("MyLog", "CategoryAdapter: bind")
            binding.tvID.setText(category.id.toString())
            binding.tvTitle.setText(category.category)
            binding.tvDate.setText(category.date)

            if (selectedItems.contains(category)){
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.red_color)
                )
            }
            else{
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.my_dark_primary)
                )
            }

            itemView.setOnClickListener {
                listener.onCategoryClick(category)
            }

            itemView.setOnLongClickListener{
                if (selectedItems.contains(category)) {
                    selectedItems.remove(category)
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.my_dark_primary)) // Сброс цвета
                } else {
                    selectedItems.add(category)
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red_color)) // Выделение
                }
                listener.onCategoryLongClick(category)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        Log.d("MyLog", "CategoryAdapter: onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("MyLog", "CategoryAdapter: getItemCount")
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        Log.d("MyLog", "CategoryAdapter: onBindViewHolder")
        holder.bind(categoryList[position], listener)
    }

    fun getCategoryList(): ArrayList<Category>{
        Log.d("MyLog", "CategoryAdapter: getCategoryList")
        return categoryList
    }

    fun addCategory(category: Category){
        Log.d("MyLog", "CategoryAdapter: addCategory()")
        Log.i("MyLog", "CategoryAdapter: addCategory: id category-${category.id}")
        categoryList.add(category)
        notifyDataSetChanged()
    }

    fun addCategory(categorys: List<Category>){
        Log.d("MyLog", "CategoryAdapter: addCategory(List)")
        for(category in categorys){
            addCategory(category)
        }
        notifyDataSetChanged()
    }

    fun clearAll() {
        Log.d("MyLog", "CategoryAdapter: clearAll")
        categoryList.clear() // Очистить список
        notifyDataSetChanged() // Сообщить адаптеру об изменениях
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

}