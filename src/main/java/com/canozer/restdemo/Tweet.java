package com.canozer.restdemo;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement
public class Tweet {
	
	private long id;
	private String username;
	private String message;
	private List<Map<String, String>> tags;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map<String, String>> getTags() {
		return tags;
	}

	public void setTags(List<Map<String, String>> tags) {
		this.tags = tags;
	}

	public String convertObjectToJSON() {
		
		Tweet tweet = new Tweet();
		tweet.id = id;
		tweet.message = message;
		tweet.tags = tags;
		tweet.username = username;
		
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jsonObject;
		try {
			
			jsonObject = new JSONObject(mapper.writeValueAsString(tweet));
			
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return null;
		
	}

	@Override
	public String toString() {
		return "Tweet [tweetID=" + id + ", tweetUser=" + username + ", tweetMessage=" + message
				+ ", tweetTags=" + tags + "]";
	}

}
