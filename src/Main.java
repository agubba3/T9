/**
 * Created by agubba on 10/22/16.
 */
public class Main {
    public static void main(String[] args) {

        Trie root = new Trie();

        //these inserts simulate a user typing the words
        //the more the inserts for a given word, the more times the user uses that word
        //and the higher its priority
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");
        root.insert("deli");

        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");
        root.insert("delta");

        root.insert("delt");
        root.insert("delt");
        root.insert("delt");
        root.insert("delt");

        root.insert("fear");
        root.insert("fear");
        root.insert("fear");
        root.insert("fear");

        root.insert("feat");
        root.insert("feat");
        root.insert("feat");

        root.insert("felix");
        root.insert("felix");
        root.insert("felix");
        root.insert("felix");
        root.insert("felix");

        //Results are in order of priority. With lower indices = higher priority and better match.
        System.out.println(root.autoCompleteT9(332));
    }
}
