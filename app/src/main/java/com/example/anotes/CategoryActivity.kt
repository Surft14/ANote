package com.example.anotes

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.databinding.ActivityCategoryBinding
import com.example.anotes.datebase.DatabaseProvider
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import com.example.anotes.view_model.click_interface.OnNoteClickListener
import com.example.anotes.view_model.adapter.CategoryAdapter
import com.example.anotes.view_model.click_interface.OnCategoryClickListener

class CategoryActivity : AppCompatActivity(), OnCategoryClickListener {
    private lateinit var binding: ActivityCategoryBinding
    private var adapter = CategoryAdapter(this)
    private lateinit var noteViewModel: NoteViewModel
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
            DatabaseProvider.getDatabase().favoriteDao()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home ->{
                Log.d("MyLog", "CategoryActivity: click home")
                finish() // Закрыть текущую Activity
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
        Toast.makeText(this, "Short click", Toast.LENGTH_LONG)
    }

    override fun onCategoryLongClick(category: Category): Boolean {
        Toast.makeText(this, "Long click", Toast.LENGTH_LONG)
        return true
    }
}