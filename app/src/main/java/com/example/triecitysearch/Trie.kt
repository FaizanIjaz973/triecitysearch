package com.example.triecitysearch

import com.example.triecitysearch.model.JsonEntry
import kotlinx.coroutines.yield
import javax.inject.Inject

class Trie @Inject constructor(){
    data class Node(
        var isWord: Boolean? = null,
        var word: String? = null,
        val childNodes: MutableMap<Char, Node> = mutableMapOf(),
        var entry: JsonEntry? = null,
        var hasASpecialChild: Boolean = false
    )

    var iterators = " abcdefghijklmnopqrstuvwxyz"
    var returnList = mutableListOf<JsonEntry>()
    val root = Node()

    fun insert(word: String, _entry: JsonEntry) {
        var currentNode = root
        for (char in word) {
            if (currentNode.childNodes[char] == null) {
                currentNode.childNodes[char] = Node()
                if(char > 'z' || char < 'a'){
                    currentNode.hasASpecialChild = true
                    if(!iterators.contains(char)){
                        iterators += char
                    }
                }
            }
            currentNode = currentNode.childNodes[char]!!
        }
        currentNode.word = word
        currentNode.isWord = true
        currentNode.entry = _entry
    }

    suspend fun startsWith(word: String): List<JsonEntry> {
        yield()
        returnList.clear()
        var currentNode = root
        for (char in word) {
            if (currentNode.childNodes[char] == null) {
                return returnList
            }
            currentNode = currentNode.childNodes[char]!!
        }
        traverseInOrder(currentNode)
        return returnList
    }

    private suspend fun traverseInOrder(_inputNode: Node){
        yield()
        val currentNode = _inputNode

        if(currentNode.hasASpecialChild) {
            for(char in iterators ) {
                if(currentNode.childNodes[char] != null)
                    traverseInOrder(currentNode.childNodes[char]!!)
            }
        }
        else
        {
            for(char in ('a'..'z' )) {
                if(currentNode.childNodes[char] != null)
                    traverseInOrder(currentNode.childNodes[char]!!)
            }
        }

        //We have reached the end of a word
        if(currentNode.isWord == true) {
            returnList.add(currentNode.entry!!)
        }
    }
}