package com.example.anotes

import android.app.Activity
import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivityMainBinding
import com.example.anotes.datebase.db_notes.NDatabaseProvider
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.adapter.NoteAdapter
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import com.example.anotes.view_model.OnNoteClickListener

class MainActivity : AppCompatActivity(), OnNoteClickListener {
    private lateinit var binding: ActivityMainBinding
    private var adapter = NoteAdapter(this)
    private lateinit var noteLauncher: ActivityResultLauncher<Intent>
    private lateinit var note: Note
    private lateinit var notes: List<Note>
    private var listNoteForDel: ArrayList<Note>? = null
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

        Log.d("MyLog", "MainActivity: Start Create ViewModel")

        // Создание репозитория
        val repository = NoteRepository(NDatabaseProvider.getDatabase().noteDao())
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "MainActivity: end Create ViewModel")

        init()
        Log.d("MyLog", "MainActivity: onCreate finish")
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
        Toast.makeText(this, "Note received: null", Toast.LENGTH_LONG).show()
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
            adapter.clearAll()
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

    //Вызов диологово окна
    fun showDeleteСonfirmationDialog(context: Context, onConfirm: () -> Unit){
        Log.d("MyLog", "MainActivity: Call dialog in MainActivity")
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.str_question_title)
        builder.setMessage(R.string.str_question_message)

        // Кнопка Да
        builder.setPositiveButton(R.string.str_yes){ dialog, _->
            Log.d("MyLog", "MainActivity: user click YES in dialog windows")
            onConfirm() // Вызов действия при подтверждении
            dialog.dismiss() // Закрыть диалог
        }

        // Кнопка "Нет"
        builder.setNegativeButton(R.string.str_no) { dialog, _ ->
            Log.d("MyLog", "MainActivity: user click NO in dialog windows")
            dialog.dismiss() // Закрыть диалог
        }

        // Создать и показать диалог
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "MainActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onNoteClick(note: Note) {
        Log.d("MyLog", "MainActivity: Call onNoteClick in MainActivity")
        Toast.makeText(this, "Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(Constant.keyNote, note)
        noteLauncher.launch(intent)
    }

    override fun onNoteLongClick(note: Note): Boolean {
        Log.d("MyLog", "MainActivity: Call onNoteLongClick in MainActivity")
        Toast.makeText(this, "Long Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "MainActivity: Long Clicked: ${note.id}, ${note.title} for delete!")
        //Инициализация массива если он пуст
        if (listNoteForDel == null){
            listNoteForDel = ArrayList()
        }

        if (listNoteForDel!!.contains(note)){// Убираем если есть в списке
            Log.i("MyLog", "MainActivity: Remove ${note.id}, ${note.title} for delete!")
            listNoteForDel!!.remove(note)
        }
        else {// Добовляем если нету в списке
            Log.i("MyLog", "MainActivity: Add ${note.id}, ${note.title} for delete!")
            listNoteForDel?.add(note)
        }
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
            R.id.mainMenuDeleteNotes -> {
                Log.d("MyLog", "MainActivity: mainMenuDeleteNotes")
                showDeleteСonfirmationDialog(this){
                    if(listNoteForDel == null){
                        Log.d("MyLog", "MainActivity: Delete all notes")
                        adapter.getNoteList().forEach { Log.i("MyLog", "Note to delete: id=${it.id}, title=${it.title}") }
                        noteViewModel.deleteAllNotesFromDB()
                        adapter.clearAll()
                        adapter.clearSelection()
                    } else {
                        Log.d("MyLog", "MainActivity: Delete selected notes")
                        Log.d("MyLog", "MainActivity: Notes for deletion: $listNoteForDel")
                        noteViewModel.deleteNotes(listNoteForDel!!)
                        adapter.clearAll()
                        //adapter.removeNotes(listNoteForDel!!)
                        adapter.clearSelection()
                        listNoteForDel = null
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}