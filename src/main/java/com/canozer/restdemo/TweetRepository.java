package com.canozer.restdemo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetRepository {
	
	Connection conn = null;
	
	public TweetRepository() {
		
		String url = "db url";
		String username = "db username";
		String password = "db password";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Tweet> getAllTweets() {
		
		List<Tweet> tweets = new ArrayList<>();
		String sql = "select * from tweet";
		
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()){
				Tweet tweet = new Tweet();
				tweet.setId(resultSet.getLong(1));
				tweet.setUsername(resultSet.getString(2));
				tweet.setMessage(resultSet.getString(3));
				
				String[] tagList = (resultSet.getString(4)).split(",");
				List<Map<String, String>> tweetTagList = new ArrayList<>();
				
				for(int i=0; i<tagList.length; i+=2){
					Map<String, String> tweetTags = new HashMap<>();
					tweetTags.put("tag", tagList[i]);
					tweetTags.put("slug", tagList[i+1]);
					tweetTagList.add(tweetTags);
				}
				
				tweet.setTags(tweetTagList);
				tweets.add(tweet);
				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tweets;
		
	}
	
	public Tweet getTweetByID(long tweetID) {
		
		String sql = "select * from tweet where tweetID=" + tweetID;
		Tweet tweet = new Tweet();
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
			if(resultSet.next()){
				
				tweet.setId(resultSet.getLong(1));
				tweet.setUsername(resultSet.getString(2));
				tweet.setMessage(resultSet.getString(3));
				
				String[] tagList = (resultSet.getString(4)).split(",");
				List<Map<String, String>> tweetTagList = new ArrayList<>();
				
				for(int i=0; i<tagList.length; i+=2){
					Map<String, String> tweetTags = new HashMap<>();
					tweetTags.put("tag", tagList[i]);
					tweetTags.put("slug", tagList[i+1]);
					tweetTagList.add(tweetTags);
				}
				
				tweet.setTags(tweetTagList);
				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tweet;
		
	}

	public void create(Tweet tweet) {
		
		String sql = "insert into tweet (tweetID, tweetUser, tweetMessage, tweetTags) values (?, ?, ?, ?)";
		
		System.err.println("inser sql: " + sql);
		
		String[] messageWords = (tweet.getMessage().split(" "));
		
		StringBuilder fullTags = new StringBuilder(); 
		
		for (int i = 0; i < messageWords.length; i++) {
			
			if(messageWords[i].startsWith("#")){
				fullTags.append((messageWords[i]).substring(1)).append(",").append((messageWords[i].substring(1)).toLowerCase()).append(",");
			}
			
		}
		
		try {
			
			System.out.println("insert into tweet (tweetUser, tweetMessage, tweetTags) values (" + tweet.getUsername() + "," + tweet.getMessage() + "," + fullTags.toString() + ")");
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setLong(1, tweet.getId());
			preparedStatement.setString(2, tweet.getUsername());
			preparedStatement.setString(3, tweet.getMessage());
			preparedStatement.setString(4, fullTags.toString());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public Boolean deleteTweet(long id, String username) {
		
		String deleteSql = "delete from tweet where tweetID = " + id;
		String tweetUsernameSql = "select tweetUser from tweet where tweetID = " + id;
		Boolean isDeleteSuccesfully = false;
		Boolean isTweetBelongToUser = false;
		
		try {
			
			Statement preparedStatementTweetUsername = conn.createStatement();
			ResultSet resultSetTweetUsername = preparedStatementTweetUsername.executeQuery(tweetUsernameSql);
			
			if(resultSetTweetUsername.next()){
				
				if(username.equals(resultSetTweetUsername.getString(1)))
					isTweetBelongToUser = true;
				
			}
			
			if(isTweetBelongToUser){
				PreparedStatement preparedStmtDeleteTweet = conn.prepareStatement(deleteSql);
				preparedStmtDeleteTweet.execute();
				isDeleteSuccesfully = true;
			}
			
			
		} catch (Exception e) {
			isDeleteSuccesfully = false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		return isDeleteSuccesfully;
	}

	public List<String> getTweetByTag(String tag, int page, String count) {
		
		List<String> tweets = new ArrayList<>();
		String sql = "select * from tweet where tweetTags like \'%" + tag + "%\'";
		
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()){
				Tweet tweet = new Tweet();
				tweet.setId(resultSet.getLong(1));
				tweet.setUsername(resultSet.getString(2));
				tweet.setMessage(resultSet.getString(3));
				
				String[] tagList = (resultSet.getString(4)).split(",");
				List<Map<String, String>> tweetTagList = new ArrayList<>();
				
				for(int i=0; i<tagList.length; i+=2){
					Map<String, String> tweetTags = new HashMap<>();
					tweetTags.put("tag", tagList[i]);
					tweetTags.put("slug", tagList[i+1]);
					tweetTagList.add(tweetTags);
				}
				
				tweet.setTags(tweetTagList);
				tweets.add(tweet.convertObjectToJSON());
				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tweets;
	}

}
