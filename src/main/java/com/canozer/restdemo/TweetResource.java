package com.canozer.restdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("twitter")
public class TweetResource {

	private static final String XUSERNAME = "x-username";

	TweetRepository tweetRepository = new TweetRepository();

	// Health-check endpoint which returns “ok” to indicate the application is
	// up.
	@GET
	@Path("healthcheck")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response healthcheck() {

		TweetResponse tweetResponse = new TweetResponse();

		tweetResponse.setStatus("ok");

		return Response.status(200).entity(tweetResponse).build();

	}

	// Extracts hashtags from given message, stores message and returns
	// structured message.
	@POST
	@Path("messages")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createTweet(Tweet tweet, @HeaderParam(XUSERNAME) String username) {

		tweet.setUsername(username);
		tweet.setId(System.currentTimeMillis());

		String[] messageWords = (tweet.getMessage().split(" "));

		StringBuilder fullTags = new StringBuilder();

		for (int i = 0; i < messageWords.length; i++) {

			if (messageWords[i].startsWith("#")) {
				fullTags.append((messageWords[i]).substring(1)).append(",")
						.append((messageWords[i].substring(1)).toLowerCase()).append(",");
			}

		}

		String[] tagList = (fullTags.toString().split(","));

		List<Map<String, String>> tweetTagList = new ArrayList<>();

		for (int i = 0; i < tagList.length; i += 2) {
			Map<String, String> tweetTags = new HashMap<>();
			tweetTags.put("tag", tagList[i]);
			tweetTags.put("slug", tagList[i + 1]);
			tweetTagList.add(tweetTags);
		}

		tweet.setTags(tweetTagList);

		tweetRepository.create(tweet);

		return Response.status(201).entity(tweet.convertObjectToJSON()).build();
	}

	// deletes message with given id. If message not exist or user is not owner
	// of the message return error
	@DELETE
	@Path("messages/{id}")
	public Response deleteTweet(@PathParam("id") long id, @HeaderParam(XUSERNAME) String username) {

		Boolean isDeleteSuccesfully = tweetRepository.deleteTweet(id, username);

		if (isDeleteSuccesfully) {
			return Response.status(204).build();
		} else {
			
			return Response.status(417).entity("There is an error while deleting to tweet.").build();
		}

	}
	
	//returns details of single message
	@GET
	@Path("messages/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTweetByID(@PathParam("id") long id) {

		Tweet tweet = tweetRepository.getTweetByID(id);

		return Response.status(200).entity(tweet.convertObjectToJSON()).build();

	}

	//filters messages with following query parameters and returns them with reverse chronological order
	@GET
	@Path("messages")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTweetFromTag(@HeaderParam("tag") String tag, @HeaderParam("page") int page, @HeaderParam("count") String count) {

		List<String> tweet = tweetRepository.getTweetByTag(tag, page, count);
		
		return Response.status(200).entity(tweet.toString()).build();

	}

	

}
