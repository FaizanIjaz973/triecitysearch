package com.example.triecitysearch

import android.content.Context
import java.io.IOException
import com.example.triecitysearch.model.JsonEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReadJsonFile (val filename:String, val context:Context){

    private lateinit var entries: List<JsonEntry>

    fun getEntries(): List<JsonEntry>{
        try {
            val jsonFileString = getJsonDataFromAsset(context, filename)
            val gson = Gson()
            val listEntryType = object : TypeToken<List<JsonEntry>>() {}.type

            if (jsonFileString == null) //File not found
                return emptyList()

            entries = gson.fromJson(jsonFileString, listEntryType)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return entries
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}