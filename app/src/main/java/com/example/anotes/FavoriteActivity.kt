package com.example.anotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivityFavoriteBinding
import com.example.anotes.datebase.DatabaseProvider
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import com.example.anotes.view_model.adapter.NoteAdapter
import com.example.anotes.view_model.click_interface.OnNoteClickListener

class FavoriteActivity : AppCompatActivity(), OnNoteClickListener {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: NoteViewModel
    private var adapter = NoteAdapter(this)
    private var listNoteForDel: ArrayList<Note>? = null
    private lateinit var favoriteLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "FavoriteActivity: onCreate start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установите Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbFavorite)
        setSupportActionBar(toolbar)

        // Включение стрелки назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        favoriteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Log.d("MyLog", "MainActivity: received result from NoteActivity: result_ok")
            }
            else if(result.resultCode == Activity.RESULT_CANCELED){
                Log.e("MyLog", "MainActivity: received result from NoteActivity: result_canceled")
            }
        }


        Log.d("MyLog", "FavoriteActivity: Start Create ViewModel")
        // Создание репозитория
        val repository = NoteRepository(
            DatabaseProvider.getDatabase().noteDao(),
            DatabaseProvider.getDatabase().categoryDao(),
        )
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "FavoriteActivity: end Create ViewModel")
        init()
        Log.d("MyLog", "FavoriteActivity: onCreate finish")
    }

    //Инициализация adapter
    fun init(){
        Log.d("MyLog", "Call function in FavoriteActivity: init")
        binding.apply {
            rvFavorite.layoutManager = GridLayoutManager(this@FavoriteActivity, 1)
            rvFavorite.adapter = adapter
            Log.d("MyLog", "Call function in FavoriteActivity: init add adapter and setting for rv")
        }
        //добовляем все заметки в rvListNotes
        Log.d("MyLog", "FavoriteActivity: Start addAllNotes from database")
        favoriteViewModel.getFavoriteListNote().observe(this){ favorite ->
            adapter.clearAll()
            if (favorite != null) {
                Log.d("MyLog", "FavoriteActivity: favorite is not null")
                adapter.addNote(favorite)
            }
            else{
                Log.e("MyLog", "FavoriteActivity: favorite is null")
            }
        }
    }
    // Добовления меню в toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "CategoryActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onNoteClick(note: Note) {
        Log.d("MyLog", "FavoriteActivity: onNoteClick")
        Toast.makeText(this, "Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@FavoriteActivity, NoteActivity::class.java)
        intent.putExtra(Constant.Key.keyNote, note)
        try{
            favoriteLauncher.launch(intent)
        }
        catch (e: Exception){
            Log.e("MyLog", "FavoriteActivity onNoteClick: Error - ${e.message}")
        }
    }

    override fun onNoteLongClick(note: Note): Boolean {
        Log.d("MyLog", "FavoriteActivity: Call onNoteLongClick in MainActivity")
        Toast.makeText(this, "Long Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "FavoriteActivity: Long Clicked: ${note.id}, ${note.title} for delete!")
        //Инициализация массива если он пуст
        if (listNoteForDel == null){
            listNoteForDel = ArrayList()
        }
        if (listNoteForDel!!.contains(note)){// Убираем если есть в списке
            Log.i("MyLog", "FavoriteActivity: Remove ${note.id}, ${note.title} for delete!")
            listNoteForDel!!.remove(note)
        }
        else {// Добовляем если нету в списке
            Log.i("MyLog", "FavoriteActivity: Add ${note.id}, ${note.title} for delete!")
            listNoteForDel?.add(note)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                Log.d("MyLog", "FavoriteActivity: click home")
                finish()
                true
            }
            R.id.favoriteDelete ->{
                Log.d("MyLog", "CategoryActivity: click categoryDelete")
                if (listNoteForDel == null){
                    Toast.makeText(this, "Выберите Заметки", Toast.LENGTH_LONG)
                }
                else{
                    Log.d("MyLog", "CategoryActivity: Delete selected notes")
                    Log.d("MyLog", "CategoryActivity: Categorys for deletion: $listNoteForDel")
                    favoriteViewModel.deleteNotes(listNoteForDel!!)
                    adapter.clearSelection()
                }
                true
            }

            else -> {true}
        }
        return super.onOptionsItemSelected(item)
    }

}