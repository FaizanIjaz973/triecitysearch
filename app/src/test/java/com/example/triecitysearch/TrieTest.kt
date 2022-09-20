package com.example.triecitysearch

import com.example.triecitysearch.model.CoordClass
import com.example.triecitysearch.model.JsonEntry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test

class TrieTest{

    private val trie = Trie()
    private val list: List<JsonEntry> = listOf(
        JsonEntry("UA", "Huzruf", 707860, CoordClass(34.283333, 44.549999)),
        JsonEntry("NL", "Hague", 152090, CoordClass(52.0705, 4.3007)),
        JsonEntry("RU", "Novinki", 519188, CoordClass(37.666668, 55.683334)),
        JsonEntry("NP", "Gorkhā", 1283378, CoordClass(84.633331, 28.0)),
        JsonEntry("NP", "Bāgmatī Zone", 1283710, CoordClass(85.416664, 28.0))
        )

    @Before
    fun generateTrie(){
        for (entry in list){
            trie.insert(entry.name, entry )
        }
    }

    @Test
    fun `Starting letter H returns city Huzruf`() = runTest{
        val entryHuzruf = JsonEntry("UA", "Huzruf", 707860, CoordClass(34.283333,44.549999))
        val result = trie.startsWith("H")
        assertThat(result).contains(entryHuzruf)
    }

    @Test
    fun `Complete word Huzruf returns city Huzruf`() = runTest {
        val entryHuzruf = JsonEntry("UA", "Huzruf", 707860, CoordClass(34.283333,44.549999))
        val result = trie.startsWith("Huzruf")
        assertThat(result).contains(entryHuzruf)
    }

    @Test
    fun `Starting letter H returns Hague as well`() = runTest {
        val entryHague = JsonEntry("NL", "Hague", 152090, CoordClass(52.0705, 4.3007))
        val result = trie.startsWith("H")
        assertThat(result).contains(entryHague)
    }

    @Test
    fun `City name contains special character(s) - 1`() = runTest {
        val entryGorkha = JsonEntry("NP", "Gorkhā", 1283378, CoordClass(84.633331, 28.0))
        val result = trie.startsWith("G")
        assertThat(result).contains(entryGorkha)
    }

    @Test
    fun `City name contains special character(s) - 2`() = runTest {
        val entryBagamti = JsonEntry("NP", "Bāgmatī Zone", 1283710, CoordClass(85.416664, 28.0))
        val result = trie.startsWith("B")
        assertThat(result).contains(entryBagamti)
    }

    @Test
    fun `Entry contains right country name`() = runTest{
        val entryNovinki = JsonEntry("RU", "Novinki", 519188, CoordClass(37.666668, 55.683334))
        val result = trie.startsWith("N")
        val entryFromResult = result[result.indexOf(entryNovinki)]
        assertThat(entryFromResult.country).isEqualTo(entryNovinki.country)
    }

}
