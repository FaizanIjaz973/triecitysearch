package com.example.triecitysearch.repo
import com.example.triecitysearch.model.JsonEntry

interface Repo {
    suspend fun readFromFile() : List<JsonEntry>
}