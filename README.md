This is a T9 Autocomplete implementation.

The data structure used was a weighted trie where each node has a count, a value which is the string so far, and a flag to mark it whether it is a terminal node (a word or not).
Trie.java works as the main class which stores all the utility functions and also serves as a class to store "Trie" objects which are basically nodes. Like said, each of these nodes has a data property, a flag, and
a count which is the number of times this was typed or inserted.

The algorithm uses a priority queue to store all relevant matches and thus sort them by the counts. Thus, the more a word is searched for, the higher the priority it will have.

Only matches according to the T9 dialpad are looked for in the trie and whenever the sequence given does not match the trie, we skip that entire branch. Thus, only the relevant nodes within the trie are looked at and their counts are kept track of for a given input. The traversal of these occurs in a manner similar to DFS where we decend deep into the trie and only keep track of matches which match the T9 input. 
Matches which occur lower than the given input in the trie are also considered as these do not match the given T9 input entirely but may potentially match. The given T9 input serves as a prefix to these results.

A note about implementation:
Currently in Main.java, words are inserted multiple times (Each on a new line to show the frequency and represent the insert atomically to mimic a user typing the word once) and the more often they are, the more the user "typed" them. I did not implement the code as if a search actually inserts the word into the trie. This could easily be changed but I decided to prepopulate the trie and then search to make the results easier to see and debug.
In the output, the words which match more based on frequency occur before words which may match less based on frequency.  
