package com.duime;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonParser;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.PlaylistItems;
import com.google.api.services.youtube.model.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

/**
 * Create a video bulletin that is posted to the user's channel feed.
 *
 * @author Jeremy Walker
 */
public class VideosChannel {

	/** Global instance properties filename. */
	private static String PROPERTIES_FILENAME = "youtube.properties";

	/**
	 * Global instance of the max number of videos we want returned (50 = upper
	 * limit per page).
	 */
	private static final long NUMBER_OF_CHANNELS_RETURNED = 1;

	/**
	 * Define a global instance of a Youtube object, which will be used to make
	 * YouTube Data API requests.
	 */
	private static YouTube youtube;

	/**
	 * Authorize the user, call the youtube.channels.list method to retrieve
	 * information about the user's YouTube channel, and post a bulletin with a
	 * video ID to that channel.
	 *
	 * @param args
	 *            command line args (not used).
	 */
	public static void main(String[] args) {

		Properties properties = new Properties();
		try {
			InputStream in = ChannelInfo.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
							+ e.getMessage());
			System.exit(1);
		}

		// This OAuth 2.0 access scope allows for full read/write access to the
		// authenticated user's account.
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

		try {
			// Authorize the request.
			Credential credential = Auth.authorize(scopes, "channelbulletin");

			// This object is used to make YouTube Data API requests.
			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
							.setApplicationName("YouTube").build();

			// Get query term from user. // PSOE, Podemos, EsquerraTV
			// String queryTerm = getInputQuery();
			List<String> queryTerms = new ArrayList<String>();
			queryTerms.add("PSOE");
			queryTerms.add("Podemos");
			queryTerms.add("Ciudadanos");
			queryTerms.add("PP");
			queryTerms.add("Vox Espana");
			queryTerms.add("EsquerraTV");
			queryTerms.add("Junts pel si");
			queryTerms.add("CUP Parlament");
			queryTerms.add("El Mundo");
			queryTerms.add("El Pais");
			queryTerms.add("Publico");
			queryTerms.add("eldiarioes");
			queryTerms.add("la razon");

			for (String queryTerm : queryTerms) {

				YouTube.Search.List search = youtube.search().list("id,snippet");
				String fileName = "./files/" + queryTerm.replace(" ", "") + "_videos.txt";
				/*
				 * It is important to set your developer key from the Google Developer Console for
				 * non-authenticated requests (found under the API Access tab at this link:
				 * code.google.com/apis/). This is good practice and increased your quota.
				 */

				System.out.println("Setting API key...");
				String apiKey = properties.getProperty("youtube.apikey");
				search.setKey(apiKey);
				search.setQ(queryTerm);
				System.out.println("OK");
				/*
				 * We are only searching for videos (not playlists or channels). If we were searching for
				 * more, we would add them as a string like this: "video,playlist,channel".
				 */
				search.setType("channel");
				/*
				 * This method reduces the info returned to only the fields we need and makes calls more
				 * efficient.
				 */
				search.setFields("items(id/kind,snippet/channelId)");
				search.setMaxResults(NUMBER_OF_CHANNELS_RETURNED);

				System.out.print("Execute search...");
				SearchListResponse searchResponse = search.execute();
				System.out.println("OK");

				SearchResult searchResult = searchResponse.getItems().iterator().next();
				String channelId = new String();

				if (searchResult != null) {
					channelId = searchResult.getSnippet().getChannelId();

					YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
					channelRequest.setId(channelId);
					channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");

					ChannelListResponse channelResult = channelRequest.execute();

					List<Channel> channelsList = channelResult.getItems();
					if (channelsList != null) {
						// The user's default channel is the first item in the list.
						// Extract the playlist ID for the channel's videos from the
						// API response.
						String uploadPlaylistId = channelsList.get(0).getContentDetails().getRelatedPlaylists()
										.getUploads();

						// Define a list to store items in the list of uploaded videos.
						List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

						// Retrieve the playlist of the channel's uploaded videos.
						YouTube.PlaylistItems.List playlistItemRequest = youtube.playlistItems()
										.list("id,contentDetails,snippet");
						playlistItemRequest.setPlaylistId(uploadPlaylistId);

						// Only retrieve data used in this application, thereby making
						// the application more efficient. See:
						// https://developers.google.com/youtube/v3/getting-started#partial
						playlistItemRequest.setFields(
										"items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");

						String nextToken = "";
						// Call the API one or more times to retrieve all items in the
						// list. As long as the API response returns a nextPageToken,
						// there are still more items to retrieve.
						System.out.println("Going to enter do loop to get all playlist items");
						int i = 0;
						do {
							playlistItemRequest.setPageToken(nextToken);
							PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

							playlistItemList.addAll(playlistItemResult.getItems());

							nextToken = playlistItemResult.getNextPageToken();
							i++;
							if (i == 120) {
								nextToken = null;
							}
							if(i%10==0) {
								System.out.println("Channel: " + queryTerm + ", iter: " + i);
							}
						} while (nextToken != null);
						System.out.println("EXIT do loop to get all playlist items");
						// Prints information about the results.
						Iterator<PlaylistItem> playlistEntries = playlistItemList.iterator();
						while (playlistEntries.hasNext()) {
							PlaylistItem playlistItem = playlistEntries.next();
							String videoID = playlistItem.getContentDetails().getVideoId();
							YouTube.Videos.List listVideosRequest = youtube.videos()
											.list("snippet,contentDetails,topicDetails,statistics").setId(videoID);
							listVideosRequest.setFields(
											"items(snippet/title,snippet/description,snippet/tags,contentDetails/duration,topicDetails,statistics)");

							VideoListResponse videoResult = listVideosRequest.execute();
							List<Video> videosList = videoResult.getItems();
							if (videosList != null) {
								// The user's default channel is the first item in the list.
								// Extract the playlist ID for the channel's videos from the
								// API response.
								Video video = videosList.get(0);
								// System.out.println("Videos: " + video.toPrettyString());

								JSONObject jsonObj = new JSONObject(video.toString());

								// try
								PrintWriter file = null;

								try {
									file = new PrintWriter(new FileOutputStream(new File(fileName), true));
									file.println(jsonObj.toString());
									// System.out.println("Successfully Copied JSON Object to File...");
									// System.out.println("\nJSON Object: " + jsonObj);
									// System.out.println("-----------------------------------------");
									file.flush();
									file.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						}
						// prettyPrint(playlistItemList.size(), playlistItemList.iterator());

					}

				}
			}

		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
							+ e.getDetails().getMessage());

			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * Returns a query term (String) from user via the terminal.
	 */
	private static String getInputQuery() throws IOException {

		String inputQuery = "";

		System.out.print("Please enter a search term: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		inputQuery = bReader.readLine();

		if (inputQuery.length() < 1) {
			// If nothing is entered, defaults to "YouTube Developers Live."
			inputQuery = "YouTube Developers Live";
		}
		return inputQuery;
	}

	/*
	 * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
	 * thumbnail.
	 *
	 * @param iteratorSearchResults Iterator of SearchResults to print
	 *
	 * @param query Search query (String)
	 */
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + NUMBER_OF_CHANNELS_RETURNED + " channels for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleChannel = iteratorSearchResults.next();
			ResourceId rId = singleChannel.getId();

			// Double checks the kind is video.
			if (rId.getKind().equals("youtube#channel")) {

				System.out.println(" Channel Id: " + singleChannel.getSnippet().getChannelId());
				System.out.println(" Channel Title: " + singleChannel.toString());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

	private static void prettyPrintChannel(Iterator<Channel> iteratorSearchResults, String query, String fileName) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + NUMBER_OF_CHANNELS_RETURNED + " channels for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			Channel singleChannel = iteratorSearchResults.next();
			System.out.println("View Count: " + singleChannel.getStatistics().getViewCount());
			System.out.println("Video Count: " + singleChannel.getStatistics().getVideoCount());
			System.out.println("Subscriber Count: " + singleChannel.getStatistics().getSubscriberCount());
			System.out.println("Summary: ");
			System.out.println(singleChannel.toString());

			System.out.println("\n-------------------------------------------------------------\n");
			JSONObject jsonObj = new JSONObject(singleChannel.toString());

			// try
			FileWriter file = null;
			try {
				file = new FileWriter(fileName, true);
				file.write(jsonObj.toString());
				System.out.println("Successfully Copied JSON Object to File...");
				System.out.println("\nJSON Object: " + jsonObj);
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private static void prettyPrintChannel(Iterator<Channel> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + NUMBER_OF_CHANNELS_RETURNED + " channels for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			Channel singleChannel = iteratorSearchResults.next();
			System.out.println("Summary: ");
			System.out.println(singleChannel.toString());

			System.out.println("\n-------------------------------------------------------------\n");

		}
	}

	/*
	* Print information about all of the items in the playlist.
	*
	* @param size size of list
	*
	* @param iterator of Playlist Items from uploaded Playlist
	*/
	private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
		System.out.println("=============================================================");
		System.out.println("\t\tTotal Videos Uploaded: " + size);
		System.out.println("=============================================================\n");

		while (playlistEntries.hasNext()) {
			PlaylistItem playlistItem = playlistEntries.next();
			System.out.println(" video name  = " + playlistItem.getSnippet().getTitle());
			System.out.println(" video id    = " + playlistItem.getContentDetails().getVideoId());
			System.out.println(" upload date = " + playlistItem.getSnippet().getPublishedAt());
			System.out.println("\n-------------------------------------------------------------\n");
		}
	}
}