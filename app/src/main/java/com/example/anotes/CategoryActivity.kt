package com.example.anotes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivityCategoryBinding
import com.example.anotes.datebase.DatabaseProvider
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import com.example.anotes.view_model.adapter.CategoryAdapter
import com.example.anotes.view_model.click_interface.OnCategoryClickListener
import java.time.LocalDate
import java.time.LocalTime


class CategoryActivity : AppCompatActivity(), OnCategoryClickListener {
    private lateinit var binding: ActivityCategoryBinding
    private var adapter = CategoryAdapter(this)
    private lateinit var noteViewModel: NoteViewModel
    private var listCategoryToDelete: ArrayList<Category>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "CategoryActivity: onCreate start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установите Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbCategory)
        setSupportActionBar(toolbar)

        // Включение стрелки назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        Log.d("MyLog", "CategoryActivity: Start Create ViewModel")
        // Создание репозитория
        val repository = NoteRepository(
            DatabaseProvider.getDatabase().noteDao(),
            DatabaseProvider.getDatabase().categoryDao(),
        )
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "CategoryActivity: end Create ViewModel")
        init()
        Log.d("MyLog", "CategoryActivity: onCreate finish")

    }
    // Добовления меню в toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "CategoryActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.category_menu, menu)
        return true
    }

    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home ->{
                Log.d("MyLog", "CategoryActivity: click home")
                finish() // Закрыть текущую Activity
                true
            }
            R.id.categoryAddNewCategorys ->{
                Log.d("MyLog", "CategoryActivity: click categoryAddNewCategorys")
                showAddCategoryDialog()
                true
            }
            R.id.categoryDelete -> {
                Log.d("MyLog", "CategoryActivity: click categoryDelete")
                if (listCategoryToDelete == null){
                    Toast.makeText(this, "Выберите категории", Toast.LENGTH_LONG)
                }
                else{
                    Log.d("MyLog", "CategoryActivity: Delete selected notes")
                    Log.d("MyLog", "CategoryActivity: Categorys for deletion: $listCategoryToDelete")
                    noteViewModel.deleteCategorys(listCategoryToDelete!!)
                    adapter.clearSelection()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun init(){
        Log.d("MyLog", "Call function in CategoryActivity: init")
        binding.apply {
            rvCategoryList.adapter = adapter
            rvCategoryList.layoutManager = GridLayoutManager(this@CategoryActivity, 1)
            Log.d("MyLog", "Call function in CategoryActivity: init add adapter and setting for rv")
        }
        Log.d("MyLog", "CategoryActivity: Start addAllCategorys from database")
        noteViewModel.getAllCategorys().observe(this){ categorys ->
            adapter.clearAll()
            if (categorys != null){
                Log.d("MyLog", "CategoryActivity: categorys is not null")
                adapter.addCategory(categorys)
            }
            else{
                Log.e("MyLog", "CategoryActivity: categorys is null")
            }
        }

    }

    override fun onCategoryClick(category: Category) {
        Log.d("MyLog", "CategoryActivity: onCategoryClick")
        val intent = Intent()
        intent.putExtra(Constant.keyCategory, category.category)
        setResult(Activity.RESULT_OK, intent)
        Toast.makeText(this, "Short click", Toast.LENGTH_LONG)
        finish()
    }

    override fun onCategoryLongClick(category: Category): Boolean {
        Log.d("MyLog", "CategoryActivity: onCategoryLongClick")
        if (listCategoryToDelete == null){
            listCategoryToDelete = ArrayList()
        }

        if (listCategoryToDelete!!.contains(category)){
            Log.i("MyLog", "CategoryActivity: Remove ${category.id}, ${category.category} for delete!")
            listCategoryToDelete!!.remove(category)
        }
        else{
            Log.i("MyLog", "CategoryActivity: Add ${category.id}, ${category.category} for delete!")
            listCategoryToDelete!!.add(category)
        }
        Toast.makeText(this, "Long click", Toast.LENGTH_LONG)
        return true
    }

    private fun showAddCategoryDialog(){
        Log.d("MyLog", "CategoryActivity: showAddCategoryDialog - start")
        // Создаем дилог
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - create dialog")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.str_add_new_category)
        // Подключаме макет
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - Connect the layout")
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null)
        builder.setView(view)
        // Инициализация элементов
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - Element initialisation")
        val edCategoryName = view.findViewById<EditText>(R.id.editTextCategoryName)
        val bAdd = view.findViewById<Button>(R.id.buttonAdd)
        val bCancel = view.findViewById<Button>(R.id.buttonCancel)
        // Создаем диалог
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - Creating a dialogue")
        val dialog = builder.create()

        // Кнопка отмена
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - Cancel button")
        try{
            bCancel.setOnClickListener {
                Log.d("MyLog", "CategoryActivity: showAddCategoryDialog - click Cancel button")
                dialog.dismiss()
            }
        }
        catch (e: Exception){
            Log.e("MyLog", "CategoryActivity: showAddCategoryDialog - Cancel button error = ${e.message}")
        }
        //Кнопка добавить
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - Add button")
        try{
            bAdd.setOnClickListener {
                Log.d("MyLog", "CategoryActivity: showAddCategoryDialog - click Add button")
                val categoryName = edCategoryName.text.toString().trim()
                if (categoryName != null) {
                    addCategoryToDB(categoryName)
                    dialog.dismiss() // Закрываем диалог
                } else {
                    Toast.makeText(this, "Введите название категории", Toast.LENGTH_SHORT).show()
                }
            }
        }
        catch (e: Exception){
            Log.e("MyLog", "CategoryActivity: showAddCategoryDialog - Add button error = ${e.message}")
        }
        Log.i("MyLog", "CategoryActivity: showAddCategoryDialog - dialog show")
        dialog.show()
        Log.d("MyLog", "CategoryActivity: showAddCategoryDialog - end")
    }

    @SuppressLint("NewApi")
    private fun addCategoryToDB(categoryName: String) {
        val category = Category(
            category = categoryName,
            timeStamp = System.currentTimeMillis(),
            time = LocalTime.now().toString(),
            date = LocalDate.now().toString(),
        )
        noteViewModel.insertCategory(category)
    }

}