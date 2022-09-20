package com.example.triecitysearch.viewmodel

import androidx.lifecycle.*
import com.example.triecitysearch.Trie
import com.example.triecitysearch.model.JsonEntry
import com.example.triecitysearch.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val trie: Trie,
    private val repo: Repo
) : ViewModel() {

    var searchJob : Job? = null
    var searchItemList = mutableListOf<JsonEntry>()

    val currentName: MutableLiveData<List<JsonEntry>> by lazy { MutableLiveData<List<JsonEntry>>() }
    val treeReadyObservableData : MutableLiveData<String> by lazy { MutableLiveData<String> () }

    private var isTreeReady: Boolean = false

    init{ readFileAndGenerateTree() }

    fun isTreeReady() : Boolean {return isTreeReady}

    private fun readFileAndGenerateTree() = viewModelScope.launch {
            yield()
            val entries = repo.readFromFile()
            if (entries.isNotEmpty()) {
                for (i in entries.indices) {
                    trie.insert(entries[i].name.lowercase(), entries[i])
                }
                isTreeReady = true
                treeReadyObservableData.postValue("true")
            }
        }

    fun searchCity(name:String){
        if(searchJob == null) {
            //Running first time
            searchJob = viewModelScope.launch {
                try {
                    yield()
                    startsWith(name)
                }catch (e: CancellationException){}
                finally {}
            }
            return
        }

        if(searchJob!!.isActive){
            searchJob!!.cancel()
        }

        searchJob = viewModelScope.launch {
            try {
                yield()
                startsWith(name)
            }catch (e: CancellationException){}
            finally { }
        }
    }

    private suspend fun startsWith(name:String) = withContext(Dispatchers.Default){
        searchItemList.clear()
        searchItemList = trie.startsWith(name) as MutableList<JsonEntry>
        currentName.postValue(searchItemList)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}