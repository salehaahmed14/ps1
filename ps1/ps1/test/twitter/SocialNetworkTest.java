/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about the@rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype @rivest @sanon1", d2);
    private static final Tweet tweet3 = new Tweet(3, "johnthelegend", "Nah! its overhyped! @bbitdiddle", d3);
    private static final Tweet tweet4 = new Tweet(4, "jiffygarfield", "Lol! continue the debate #hype @hellen", d1);
    private static final Tweet tweet5 = new Tweet(5, "kellykaren", "This isn't very informative @bbitdiddle", d1);
    private static final Tweet tweet6 = new Tweet(5, "kellykaren", "This isn't very informative @box @simHi", d1);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /* Verifies that when an empty list of tweets is provided, the guessFollowsGraph function returns an empty graph.*/
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    /*Ensures that when there are tweets with no mentions in the list, the guessFollowsGraph function returns an empty graph.*/
    @Test
    public void testGuessFollowsGraphTweetsNoMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    /*Checks that the guessFollowsGraph function correctly identifies users who are mentioned in tweets, and associates 
     * them with the mentioned users in the graph.*/
    @Test
    public void testGuessFollowsGraphTweetsOneMentionEach() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3,tweet5));
        Set<String> expectedFollows = new HashSet<>();
        expectedFollows.add("bbitdiddle");
        assertEquals(expectedFollows, followsGraph.get("johnthelegend"));
        assertEquals(expectedFollows, followsGraph.get("kellykaren"));
    }
    
    /*Validates that the guessFollowsGraph function correctly associates multiple mentioned users with the authors of the tweets.*/
    @Test
    public void testGuessFollowsGraphTweetsMultipleMentionEach() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3,tweet2));
        Set<String> expectedFollows1 = new HashSet<>();
        Set<String> expectedFollows2 = new HashSet<>();
        expectedFollows1.add("bbitdiddle");
        expectedFollows2.add("rivest");
        expectedFollows2.add("sanon1");
        assertEquals(expectedFollows1, followsGraph.get("johnthelegend"));
        assertEquals(expectedFollows2, followsGraph.get("bbitdiddle"));
    }
    
    /*Verifies that the guessFollowsGraph function handles multiple tweets by the same author
     *  and associates all mentioned users with the author in the graph.*/
    @Test
    public void testGuessFollowsGraphMultipleTweetsSamePerson() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5,tweet6));
        Set<String> expectedFollows = new HashSet<>();
        expectedFollows.add("bbitdiddle");
        expectedFollows.add("box");
        expectedFollows.add("simhi");
        assertEquals(expectedFollows, followsGraph.get("kellykaren"));
    }
    
    /*Checks that when the input followsGraph is empty, the influencers function returns an empty list of influencers.*/
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    /*Verifies that when there's only one user in the followsGraph with no followers, the influencers function returns an empty list.*/
    @Test
    public void testInfluencersSingleUser() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        List<String> result = SocialNetwork.influencers(followsGraph);
        assertEquals(0, result.size());
    }
    
    /*Ensures that when there's a single influencer in the followsGraph, the influencers function correctly identifies and
     *returns that influencer as the only user in the list.*/
    @Test
    public void testInfluencersSingleInfluencer() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4));
        List<String> result = SocialNetwork.influencers(followsGraph);
        assertEquals(1, result.size());
        assertEquals("hellen", result.get(0));
    }

    /* Checks that the influencers function can correctly identify and return the top influencers when there
     * are multiple users in the followsGraph with varying numbers of followers.*/
    @Test
    public void testInfluencersMultipleUsers() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5,tweet2,tweet3));
    	List<String> result = SocialNetwork.influencers(followsGraph);
    	assertEquals(3, result.size());
        assertEquals("bbitdiddle", result.get(0));
        assertEquals("rivest", result.get(1));
        assertEquals("sanon1", result.get(2));
    }

    /*Validates that the influencers function correctly handles cases where multiple users 
     * have an equal number of followers and returns them in any order.*/
    @Test
    public void testInfluencersEqualFollowers() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5,tweet6));
    	List<String> result = SocialNetwork.influencers(followsGraph);
    	assertEquals(3, result.size());
        assertTrue(result.contains("bbitdiddle"));
        assertTrue(result.contains("box"));
        assertTrue(result.contains("simhi"));
    }
    
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
