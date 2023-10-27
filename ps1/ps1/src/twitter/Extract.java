/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;

import java.util.HashSet;
/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
	//helper function to sort the list of tweets depending upon the timestamp
	private static List<Tweet> sortByTimestamp(List<Tweet> tweets) {
        assert tweets != null;
        
        Comparator<Tweet> byTimestamp = new Comparator<Tweet>() {
            @Override public int compare(Tweet tweet1, Tweet tweet2) {
                return tweet1.getTimestamp().compareTo(tweet2.getTimestamp());
            }            
        };
        
        return tweets.stream()
                .sorted(byTimestamp)
                .collect(Collectors.toList());
    }
	public static Timespan getTimespan(List<Tweet> tweets) {
		assert tweets != null;
		List<Tweet> sortedList = sortByTimestamp(tweets);
		Instant start = sortedList.get(0).getTimestamp();
        Instant end = sortedList.get(sortedList.size() -1).getTimestamp();
        
        return new Timespan(start, end);
    }
    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	Set<String> MentionedUsersSet = new HashSet<>();
    	
    	// Define a regular expression pattern(alphabets A-Z/a-z numbers 0-9 characters -/_ starts and ends with spaces)
    	String validMentionRegex = "\\B@[a-zA-Z0-9_-]+\\b";
    	// Compile the regular expression pattern into a Pattern object
        Pattern p = Pattern.compile(validMentionRegex);
        
        for (Tweet tweet: tweets) {
            String text = tweet.getText();
            java.util.regex.Matcher m = p.matcher(text);
            
            while (m.find()) {
                String userMentioned = m.group().substring(1).toLowerCase();
                if (!MentionedUsersSet .contains(userMentioned)) {
            		MentionedUsersSet.add(userMentioned);
               }
            }
        }
        
    	assert MentionedUsersSet != null;
    	return MentionedUsersSet ;
    }

}
