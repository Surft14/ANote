package com.example.anotes.view_model.adapter

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anotes.R
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.CategoryItemBinding
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.view_model.click_interface.OnCategoryClickListener
import kotlin.collections.ArrayList

class CategoryAdapter(private val listener: OnCategoryClickListener): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    private var categoryList = ArrayList<Category>()
    private val selectedItems = mutableSetOf<Category>() // Хранит выделенные заметки
    inner class CategoryHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = CategoryItemBinding.bind(item)
        fun bind(category: Category, listener: OnCategoryClickListener){
            Log.d("MyLog", "CategoryAdapter: bind")
            binding.tvID.setText(category.id.toString())
            binding.tvCategory.setText(category.category)

            if(category.category.length >= Constant.CountChars.char && category.category.length <= Constant.CountChars.chars5){// от 1 до 5
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)// 34sp
            }
            else if (category.category.length > Constant.CountChars.chars5 && category.category.length <= Constant.CountChars.chars10){ // от 5 до 10
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)// 30sp
            }
            else if (category.category.length > Constant.CountChars.chars10 && category.category.length <= Constant.CountChars.chars15){ // от 10 до 15
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)// 25sp
            }
            else if (category.category.length > Constant.CountChars.chars15 && category.category.length <= Constant.CountChars.chars20){ // от 15 до 20
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)// 25sp
            }
            else if (category.category.length > Constant.CountChars.chars20 && category.category.length <= Constant.CountChars.chars25){ // от 20 до 25
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)// 18sp
            }
            else {// больше 25
                binding.tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)// 16sp
            }


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
        try{
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
            return CategoryHolder(view)
        }
        catch (e:Exception){
            Log.e("MyLog", "CategoryAdapter: onCreateViewHolder end with error ${e.message}")
            return TODO()
        }
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
        Log.i("MyLog", "CategoryAdapter: addCategory: id category-${category.id}, name category-${category.category}")
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