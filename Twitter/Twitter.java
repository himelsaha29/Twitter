import java.util.ArrayList;

/**
 *
 * @author HimelSaha
 */


public class Twitter {

    private MyHashTable<String, Tweet> authorTbl = null;
    private MyHashTable<String, ArrayList<Tweet>> dateTbl = null;
    private MyHashTable<String, String> stopTbl = null;
    private MyHashTable<String, Integer> wordsTbl = null;
    private ArrayList<Tweet> tweets;
    private ArrayList<String> stopWords;

    ArrayList<Tweet> tweetsArr = null;

    // O(n+m) where n is the number of tweets, and m the number of stopWords
    public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
        this.tweets = tweets;
        this.stopWords = stopWords;
        authorTbl = new MyHashTable<String, Tweet>(tweets.size() + 1);
        dateTbl = new MyHashTable<String, ArrayList<Tweet>>(tweets.size() + 1);
        stopTbl = new MyHashTable<>(tweets.size() + 1);

        for (String s : stopWords) {
            stopTbl.put(s.trim().toLowerCase(), "Stopword");
        }

        for (Tweet t : tweets) {
            if (authorTbl.get(t.getAuthor()) != null) {
                if (authorTbl.get(t.getAuthor()).compareTo(t) >= 0) {

                } else {
                    authorTbl.put(t.getAuthor(), t);
                }

            } else {
                authorTbl.put(t.getAuthor(), t);
            }

            if (dateTbl.get(t.getDateAndTime().substring(0, 10)) != null) {
                dateTbl.get(t.getDateAndTime().substring(0, 10)).add(t);
            } else {
                ArrayList<Tweet> x = new ArrayList<>();
                x.add(t);
                dateTbl.put(t.getDateAndTime().substring(0, 10), x);
            }

        }
    }

    /**
     * Add Tweet t to this Twitter O(1)
     */
    public void addTweet(Tweet t) {
        this.tweets.add(t);

        if (authorTbl.get(t.getAuthor()) != null) {
            if (authorTbl.get(t.getAuthor()).compareTo(t) >= 0) {

            } else {
                authorTbl.put(t.getAuthor(), t);
            }
        } else {
            authorTbl.put(t.getAuthor(), t);
        }

        if (dateTbl.get(t.getDateAndTime().substring(0, 10)) != null) {
            dateTbl.get(t.getDateAndTime().substring(0, 10)).add(t);
        } else {
            ArrayList<Tweet> x = new ArrayList<>();
            x.add(t);
            dateTbl.put(t.getDateAndTime().substring(0, 10), x);
        }
    }

    /**
     * Search this Twitter for the latest Tweet of a given author. If there are
     * no tweets from the given author, then the method returns null. O(1)
     */
    public Tweet latestTweetByAuthor(String author) {
        Tweet x = authorTbl.get(author);

        return x;
    }

    /**
     * Search this Twitter for Tweets by `date' and return an ArrayList of all
     * such Tweets. If there are no tweets on the given date, then the method
     * returns null. O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        return dateTbl.get(date);

    }

    /**
     * Returns an ArrayList of words (that are not stop words!) that appear in
     * the tweets. The words should be ordered from most frequent to least
     * frequent by counting in how many tweet messages the words appear. Note
     * that if a word appears more than once in the same tweet, it should be
     * counted only once.
     */
    public ArrayList<String> trendingTopics() {
        wordsTbl = new MyHashTable<String, Integer>(tweets.size() + 1);
        for (Tweet t : tweets) {

            MyHashTable<String, Integer> tempTbl = new MyHashTable<String, Integer>(tweets.size() + 1);
            for (String str : getWords(t.getMessage())) {

                if (stopTbl.get(str.trim().toLowerCase()) != null) {
                    continue;
                }

                tempTbl.put(str.trim().toLowerCase(), 1);
            }

            for (HashPair<String, Integer> temp : tempTbl) {

                if ((wordsTbl.get(temp.getKey())) != null) {
                    wordsTbl.put(temp.getKey(), (wordsTbl.get(temp.getKey())) + 1);

                } else if ((wordsTbl.get(temp.getKey())) == null) {
                    wordsTbl.put(temp.getKey().trim().toLowerCase(), 1);
                }
            }
        }
        return MyHashTable.fastSort(wordsTbl);
    }

    /**
     * An helper method you can use to obtain an ArrayList of words from a
     * String, separating them based on apostrophes and space characters. All
     * character that are not letters from the English alphabet are ignored.
     */
    private static ArrayList<String> getWords(String msg) {
        msg = msg.replace('\'', ' ');
        String[] words = msg.split(" ");
        ArrayList<String> wordsList = new ArrayList<String>(words.length);
        for (int i = 0; i < words.length; i++) {
            String w = "";
            for (int j = 0; j < words[i].length(); j++) {
                char c = words[i].charAt(j);
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                    w += c;
                }

            }
            wordsList.add(w);
        }
        return wordsList;
    }

}
