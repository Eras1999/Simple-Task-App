package com.example.taskapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSelectDate: Button
    private lateinit var textViewDate: TextView
    private lateinit var buttonSave: Button
    private lateinit var listViewTasks: ListView

    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSelectDate = findViewById(R.id.buttonSelectDate)
        textViewDate = findViewById(R.id.textViewDate)
        buttonSave = findViewById(R.id.buttonSave)
        listViewTasks = findViewById(R.id.listViewTasks)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listViewTasks.adapter = adapter

        sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE)

        loadTasks()

        buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                textViewDate.text = date
            }, year, month, day)

            datePickerDialog.show()
        }

        buttonSave.setOnClickListener {
            val description = editTextDescription.text.toString()
            val date = textViewDate.text.toString()

            if (description.isNotEmpty() && date != "Date not selected") {
                val task = Task(description, date)
                tasks.add(task)
                adapter.add("${task.description} - ${task.date}")
                adapter.notifyDataSetChanged()
                saveTasks()

                editTextDescription.text.clear()
                textViewDate.text = "Date not selected"
            } else {
                Toast.makeText(this, "Please enter a description and select a date", Toast.LENGTH_SHORT).show()
            }
        }

        listViewTasks.setOnItemClickListener { _, _, position, _ ->
            tasks.removeAt(position)
            adapter.remove(adapter.getItem(position))
            adapter.notifyDataSetChanged()
            saveTasks()
        }
    }

    private fun loadTasks() {
        val tasksSet = sharedPreferences.getStringSet("tasks", emptySet())
        tasksSet?.forEach {
            val task = Task.fromString(it)
            tasks.add(task)
            adapter.add("${task.description} - ${task.date}")
        }
    }

    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        val tasksSet = tasks.map { it.toString() }.toSet()
        editor.putStringSet("tasks", tasksSet)
        editor.apply()
    }
}
