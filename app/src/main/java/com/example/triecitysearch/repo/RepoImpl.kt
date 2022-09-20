package com.example.triecitysearch.repo

import android.content.Context
import com.example.triecitysearch.ReadJsonFile
import com.example.triecitysearch.model.JsonEntry
import javax.inject.Inject

class RepoImpl @Inject constructor(val context: Context) : Repo {

    override suspend fun readFromFile() : List<JsonEntry> {
        context
        val fileName = "cities1.json" //Name of the file location in assets folder
        var readJsonFileObj = ReadJsonFile(fileName, context)
        return readJsonFileObj.getEntries()
        return listOf()
    }

}