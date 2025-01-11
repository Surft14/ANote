package com.example.anotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.constant.Constant
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
    private lateinit var noteLauncher: ActivityResultLauncher<Intent>
    private lateinit var note: Note
    private var notes: List<Note>? = null
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "MainActivity: onCreate start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установите Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbMain)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //получаем и обробатываем результаты полученные с других активити
        noteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Log.d("MyLog", "MainActivity: received result from NoteActivity: result_ok")
                note = result.data?.getSerializableExtra(Constant.keyNote) as Note
                adapter.clearAll()
                toastCall(note)
            }
            else if(result.resultCode == Activity.RESULT_CANCELED){
                Log.e("MyLog", "MainActivity: received result from NoteActivity: result_canceled")
                toastErrorCall()
            }
        }
        Log.d("MyLog", "MainActivity: onCreate finish")
        Log.d("MyLog", "NoteActivity: Start Create ViewModel")

        // Создание репозитория
        val repository = NoteRepository(DatabaseProvider.getDatabase().noteDao())
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "NoteActivity: end Create ViewModel")

        init()
    }
    //Всплывающие сообщение что заметка добавлина
    private fun toastCall(note: Note){
        Log.d("MyLog", "Call function in MainActivity: toastCall")
        Toast.makeText(this, "Note received: id-${note.id}, tittle-${note.title}, time-${note.time}", Toast.LENGTH_LONG).show()
        Log.i("MyLog", "Result: id-${note.id} tittle-${note.title}, time-${note.time}, date-${note.date}")
    }
    //Всплывающие сообщение об ошибке
    private fun toastErrorCall(){
        Log.d("MyLog", "Call function in MainActivity: toastCall")
        Toast.makeText(this, "Note received: Error", Toast.LENGTH_LONG).show()
    }
    //Инициализация rvListNote
    @Suppress("UNCHECKED_CAST")
    private fun init(){
        Log.d("MyLog", "Call function in MainActivity: init")
        binding.apply {
            rvListNotes.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rvListNotes.adapter = adapter
            Log.d("MyLog", "Call function in MainActivity: init add adapter and setting for rv")
        }

        //добовляем все заметки в rvListNotes
        Log.d("MyLog", "MainActivity: Start addAllNotes from database")
        noteViewModel.getAllNotes().observe(this) { notes ->
            if (notes != null) {
                Log.d("MyLog", "MainActivity: notes is not null")
                adapter.addNote(notes)
            }
            else{
                Log.e("MyLog", "MainActivity: notes is null")
            }
        }
        /*notes = noteViewModel.getAllNotes().value
        if (notes != null) {
            Log.d("MyLog", "Notes ready")
            adapter.addNote(notes!!)
        } else {
            Log.e("MyLog", "Notes are not ready yet")
        }*/

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "MainActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainMenuNewNote -> {
                // Переход на NoteActivity
                Log.d("MyLog", "MainActivity: menuNewNote")
                val intent = Intent(this@MainActivity, NoteActivity::class.java)
                noteLauncher.launch(intent)
                return true
            }
            R.id.mainMenuSearchNote ->{
                // Переход на SearchActivity
                Log.d("MyLog", "MainActivity: menuSearchNote")
                val  intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}