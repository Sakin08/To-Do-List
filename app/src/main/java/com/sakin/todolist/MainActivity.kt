package com.sakin.todolist

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var item: EditText
    private lateinit var add: Button
    private lateinit var listView: ListView

    private var itemList = ArrayList<Task>()
    private val fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        item = findViewById(R.id.item)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.listview)

        // Load existing tasks from file
        itemList = fileHelper.readData(this)

        // Set up ArrayAdapter for the ListView
        val arrayAdapter = object : ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, itemList) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val task = getItem(position)

                val textView = view.findViewById<TextView>(android.R.id.text1)

                // Add numbering to tasks
                textView.text = "${position + 1}. ${task?.name}"

                // Strike-through for completed tasks
                if (task?.isCompleted == true) {
                    textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

                return view
            }
        }
        listView.adapter = arrayAdapter

        // Add new task to the list
        add.setOnClickListener {
            val itemName = item.text.toString()
            if (itemName.isNotBlank()) {
                val newTask = Task(itemName, false) // New task starts as incomplete
                itemList.add(newTask)
                item.setText("") // Clear the input field
                fileHelper.writeData(itemList, applicationContext) // Save to file
                arrayAdapter.notifyDataSetChanged() // Refresh the list
            } else {
                Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show()
            }
        }

        // Toggle task completion on click
        listView.setOnItemClickListener { parent, _, position, _ ->
            val clickedTask = parent.getItemAtPosition(position) as Task
            clickedTask.isCompleted = !clickedTask.isCompleted // Toggle completion state
            fileHelper.writeData(itemList, applicationContext) // Save updated list
            arrayAdapter.notifyDataSetChanged() // Refresh the list
        }

        // Delete a task on long click
        listView.setOnItemLongClickListener { parent, _, position, _ ->
            val clickedTask = parent.getItemAtPosition(position) as Task
            AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { dialog, _ ->
                    itemList.removeAt(position) // Remove the task
                    fileHelper.writeData(itemList, applicationContext) // Save updated list
                    arrayAdapter.notifyDataSetChanged() // Refresh the list
                    Toast.makeText(this, "${clickedTask.name} deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
            true
        }
    }
}
