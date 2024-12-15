package com.sakin.todolist

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var item: EditText
    lateinit var add: Button
    lateinit var listView: ListView

    var itemList = ArrayList<String>()
    val fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        item = findViewById(R.id.item)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.listview)

        // Read existing data from file
        itemList = fileHelper.readData(this)

        // Set up ArrayAdapter
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = arrayAdapter

        // Add item to the list
        add.setOnClickListener {
            val itemName = item.text.toString()
            if (itemName.isNotBlank()) {
                itemList.add(itemName)
                item.setText("") // Clear input field
                fileHelper.writeData(itemList, applicationContext) // Save to file
                arrayAdapter.notifyDataSetChanged() // Update list view
            } else {
                Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle item click to delete
        listView.setOnItemClickListener { parent, _, position, _ ->
            val clickedItem = parent.getItemAtPosition(position) as String

            // Create and show AlertDialog
            AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete this item from the list?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    itemList.removeAt(position)
                    arrayAdapter.notifyDataSetChanged() // Update the list view
                    fileHelper.writeData(itemList, applicationContext) // Save updated data
                    Toast.makeText(this, "$clickedItem deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss() // Dismiss dialog
                }
                .show()
        }
    }
}
