package com.example.anotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.databinding.ActivityMainBinding
import com.example.anotes.db_notes.DatabaseProvider
import com.example.anotes.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteAdapter
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter = NoteAdapter()
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "onCreate MainActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NoteRepository(DatabaseProvider.getDatabase(applicationContext).noteDao())

        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        // Установите Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbMain)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        noteViewModel.getAllNotes().observe(this) { note ->
            // Обновляем список заметок
            editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if (it.resultCode == RESULT_OK){
                    updateRecyclerView(it.data?.getSerializableExtra("note") as Note)
                }
            }
        }



    }

    private fun updateRecyclerView(note: Note) {
        adapter.addNote(note)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "onCreateOptionsMenu MainActivity")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewNote -> {
                // Переход на NoteActivity
                Log.d("MyLog", "menuNewNote MainActivity")
                editLauncher?.launch(Intent(this@MainActivity, NoteActivity::class.java))
                return true
            }
            R.id.menuSearchNote ->{
                // Переход на SearchActivity
                Log.d("MyLog", "menuSearchNote MainActivity")
                val  intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init(){
        binding.rvListNotes.layoutManager = GridLayoutManager(this@MainActivity, 1)
        binding.rvListNotes.adapter = adapter

    }

}