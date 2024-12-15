package com.sakin.todolist

import android.content.Context
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper {

    val FILENAME = "listinfo.dat"

    // Write data to file
    fun writeData(item: ArrayList<Task>, context: Context) {
        try {
            val fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            val oas = ObjectOutputStream(fos)
            oas.writeObject(item)
            oas.close()
        } catch (e: Exception) {
            e.printStackTrace() // Log any errors during file writing
        }
    }

    // Read data from file
    fun readData(context: Context): ArrayList<Task> {
        var listItem = ArrayList<Task>()
        try {
            val fis: FileInputStream = context.openFileInput(FILENAME)
            val ois = ObjectInputStream(fis)
            listItem = ois.readObject() as ArrayList<Task>
            ois.close()
        } catch (e: FileNotFoundException) {
            // Return an empty list if the file does not exist
            listItem = ArrayList()
        } catch (e: Exception) {
            e.printStackTrace() // Log any errors during file reading
        }
        return listItem
    }
}
