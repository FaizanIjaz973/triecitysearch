# triecitysearch
Trie city search android project

We have a list of cities containing around 200k entries in JSON format. Each entry contains the following information:
{
    "country":"UA",
    "name":"Hurzuf",
    "_id":707860,
    "coord":{
        "lon":34.283333,
        "lat":44.549999
    }
}
The app implements the following:

- Load the list of cities from a local file.
- Filters the results by a given prefix string, following these requirements:
- Following the prefix definition specified in the clarifications section below.
- Implements a trie search algorithm optimised for fast runtime searches.
- Search is case insensitive.
- Time efficiency for filter algorithm is better than linear
- Displays these cities in a scrollable list, in alphabetical order (city first, country after). Hence, "Denver, US" appears before "Sydney, Australia".
- The UI is responsive while typing in a filter.
- The list updates with every character added/removed to/from the filter.
- Each city's cell does:
  - Show the city and country code as title.
  - When tapped, show the location of that city on a map.
- Unit tests are provided that show the search algorithm is displaying the correct results giving different inputs.

The app uses Dagger Hilt for dependency injection to make testing easier and more robust. Additional tests can be added as required. 

The fundamental function of the app is to implement the trie search which returns results in milliseconds once the trie is properly construced and ready to be searched. 
Refer to Trie.kt class for the implementation. 
