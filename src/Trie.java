/**
 * Created by agubba on 10/22/16.
 */
import java.util.*;
import java.util.Map.Entry;

public class Trie {

    protected final Map<Character, Trie> children;
    protected String value;
    protected boolean terminal = false;
    protected int count;

    public static HashMap<Integer, HashSet<Character>> dict;

    public Trie() {
        this(null);
    }

    /**
     * This is the constructor which is called within the class.
     * The T-9 data is initialized according to:
     * https://upload.wikimedia.org/wikipedia/commons/7/73/Telephone-keypad2.svg
     * @param value
     */
    private Trie(String value) {
        this.value = value;
        dict = new HashMap<>();
        HashSet<Character> set0 = new HashSet<>();
        set0.add('a');
        set0.add('b');
        set0.add('c');
        dict.put(2, set0);
        HashSet<Character> set1 = new HashSet<>();
        set1.add('d');
        set1.add('e');
        set1.add('f');
        dict.put(3, set1);
        HashSet<Character> set2 = new HashSet<>();
        set2.add('g');
        set2.add('h');
        set2.add('i');
        dict.put(4, set2);
        HashSet<Character> set3 = new HashSet<>();
        set3.add('j');
        set3.add('k');
        set3.add('l');
        dict.put(5, set3);
        HashSet<Character> set4 = new HashSet<>();
        set4.add('m');
        set4.add('n');
        set4.add('o');
        dict.put(6, set4);
        HashSet<Character> set5 = new HashSet<>();
        set5.add('p');
        set5.add('q');
        set5.add('r');
        set5.add('s');
        dict.put(7, set5);
        HashSet<Character> set6 = new HashSet<>();
        set6.add('t');
        set6.add('u');
        set6.add('v');
        dict.put(8, set6);
        HashSet<Character> set7 = new HashSet<>();
        set7.add('w');
        set7.add('x');
        set7.add('y');
        set7.add('z');
        dict.put(9, set7);

        if (value != null) this.count = 0;
        children = new HashMap<>();
    }

    /**
     * This represents the string value for this given node as the value so far + the character to tbe added
     * Then the reference is assigned.
     * @param c
     */
    protected void add(char c) {
        String val;
        if (this.value == null) {
            val = Character.toString(c);
        } else {
            val = this.value + c;
        }
        children.put(c, new Trie(val));
    }

    /**
     * This method actually inserts the word into the trie
     * It uses the add method which actually creates a new node and
     * assigns the reference.
     * @param word
     */
    public void insert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Cannot add null to a Trie");
        }
        Trie node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.count++;
        node.terminal = true;
    }

    /**
     * This just returns if a word in the dictionary exists
     * @param word
     * @return
     */
    public boolean find(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return true;
    }

    /**
     * This is the autocomplete methid for the normal non T-9 method
     * It just, given a dictionary, returns the matches sorted.
     * @param prefix
     * @return
     */
    public Collection<String> autoComplete(String prefix) {
        Trie node = this;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        PriorityQueue<Trie> nodesOrdered = node.allPrefixes();
        ArrayList<String> out = new ArrayList<>();
        int origSize = nodesOrdered.size();
        for (int i = 0; i < origSize; i++) {
            out.add(nodesOrdered.poll().value);
        }
        return out;
    }

    /**
     * Recursively get all the below words. Similar to a dfs.
     * This is only applied to a non T-9 approach. This basically goes down the trie until the pattern is matched and then
     * gets all the matches below. This fails for T-9.
     * @return
     */
    protected PriorityQueue<Trie> allPrefixes() {
        PriorityQueue<Trie> results = new PriorityQueue<>(10, (Trie a, Trie b) -> b.count - a.count);
        if (this.terminal) {
            results.add(this);
        }
        for (Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            PriorityQueue<Trie> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }

    /**
     * The autocomplete method for T-9 which is called where given a prefix, the most
     * prevalent matches are returned. The matches are sorted by how many times they were searched for
     * or in this case, how many times they were inserted.
     * We also dequeue from the priority queue and return the results in a collection.
     * @param prefix
     * @return
     */
    public Collection<String> autoCompleteT9(int prefix) {
        Trie node = this;
        int l = String.valueOf(prefix).length();
        PriorityQueue<Trie> nodesOrdered = node.allPrefixesT9(prefix, l - 1, false);
        ArrayList<String> out = new ArrayList<>();
        int origSize = nodesOrdered.size();
        for (int i = 0; i < origSize; i++) {
            out.add(nodesOrdered.poll().value);
        }
        return out;
    }

    /**
     * This is the helper method which actually returns the matches
     * The logic is as follows:
     * In the trie, if a given character does not match from the sequence, that branch (all words below) is not considered - line 225
     * If a match exists (meaning the T-9 keyboard contains a char for the given digit from the sequence which is in the trie,
     * then we recurse downwards and keep checking if matches exist and at any point, if the algo hits a terminal node along the way or it has reached
     * the end of the sequence and a terminal node is present, we add that word to a priority queue which sorts incrementally with each add
     * by the word's count.
     * Once the sequence is exhausted (when cur drops below 0), we can skip the checking if the given character in the trie matches
     * the T-9 keyboard for then given int within the sequence. Instead, all matches below that sequence can be taken and can be added to the same priority queue.
     * This priority queue thus adds n elements each taking log(n) time. This the entire operation takes nlog(n) time.
     * Only the branches in the trie which matche the T9 sequence are traversed and therefore, this algorithm is divide and conquer in nature.
     * @param t9Seq
     * @param cur
     * @param pastCheck
     * @return
     */
    protected PriorityQueue<Trie> allPrefixesT9(int t9Seq, int cur, boolean pastCheck) {
        PriorityQueue<Trie> results = new PriorityQueue<>(10, (Trie a, Trie b) -> b.count - a.count);
        if (this.terminal && cur < 0) {
            pastCheck = true;
            results.add(this);
        } else if (cur < 0) {
            pastCheck = true; //if you have reached the end of your expression and there is more to see
        }
        int curDigit = 2; //just to shut the compiler up
        if (cur < 0 && !pastCheck) {
            return results;
        } else if (!pastCheck) {
            curDigit = t9Seq / (int) Math.pow(10, cur);
            curDigit = curDigit % 10;
        }
        //just return out if cur is < 0 because the actual sequence is only so long.
        //starts off as first digit
        for (Entry<Character, Trie> entry : children.entrySet()) {
            if (!pastCheck && !dict.get(curDigit).contains(entry.getKey())) continue;
            Trie child = entry.getValue();
            PriorityQueue<Trie> childPrefixes = child.allPrefixesT9(t9Seq, cur - 1, pastCheck);
            results.addAll(childPrefixes);
        }
        return results;
    }
}