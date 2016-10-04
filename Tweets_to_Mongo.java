package twitter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;


public class Tweets_to_Mongo {
	@SuppressWarnings({ "unused", "unchecked" })
	public static void main(String[] args) throws Exception {

		MongoClient mongo1 = new MongoClient("Localhost",27017);
		MongoDatabase db = mongo1.getDatabase("French_Tea_Analysis");
		long userId = 0000000L;
// Delete any existing collection named "sortedTweets	"
		db.getCollection("sample2").drop();

// Create a new collection
		MongoCollection<Document> collection = db.getCollection("twinings");

// The file we wish to input into our database
		File file = new File("Tweets/French_Tea_Data/old_data/twinings.json");

// Our reader
		BufferedReader br = new BufferedReader(new FileReader(file));

		int counter = 0;
		String line;

		while ((line = br.readLine()) != null) {
			Document old_status = Document.parse(line);
			
// If it is a deleted tweet, then continue
		if (old_status.containsKey("delete")) {
			continue;
		}
		ArrayList<String> original_tweet_info = new ArrayList<String>();
		
//populate original tweets		
		Document original_status = new Document();

			original_status.append("status",old_status.get("text"));
			original_status.append("Likes",old_status.getInteger("favorite_count"));
			original_status.append("RetweetCount", old_status.getInteger("retweet_count"));
//Date
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy"); 
			String d = old_status.get("created_at").toString();
			original_status.append("created_at", df.parse(d));
			
			Document user = (Document) old_status.get("user");
			original_status.append("UniqueID", user.getString("id_str"));
			original_status.append("screen_name", user.getString("screen_name"));
			original_status.append("Name", user.getString("name"));
			original_status.append("lang", user.getString("lang"));
			original_status.append("statuses_count", user.getInteger("statuses_count"));
			original_status.append("friends_count", user.getInteger("friends_count"));
			original_status.append("followers_count", user.getInteger("followers_count"));
			original_status.append("Description",user.getString("description"));
			original_status.append("locationGEO", user.getString("location"));
			original_status.append("Timezone", user.getString("time_zone"));
			
//Hashtags		
		Document entity = (Document)old_status.get("entities");
		ArrayList<Document> hashlist =(ArrayList<Document>) entity.get("hashtags");
		ArrayList<String> hashtaglist = new ArrayList<String>();
		for(Document hashtag:hashlist){
			String hash = hashtag.getString("text");
			hashtaglist.add(hash);
		}
		original_status.append("Hashtags",hashtaglist);

//Mentions
		ArrayList<Document> mentions =(ArrayList<Document>) entity.get("user_mentions");
		ArrayList<String> mentionlist = new ArrayList<String>();
		for(Document mention:mentions){
			String g = mention.getString("screen_name");
			mentionlist.add(g);
		}
		original_status.append("User_Mention",mentionlist);
			
//External URL
		ArrayList<Document> URL = (ArrayList<Document>) entity.get("urls");
		ArrayList<String> display_url = new ArrayList<String>();
		for(Document url:URL){
			String link = url.getString("display_url");
			display_url.add(link);
		}
		original_status.append("External_WebLink",display_url);

//Retweeted_status		
		if(line.contains("retweeted_status")){
			Document retweet_status = new Document();
			Document get_status = (Document)old_status.get("retweeted_status");
			String text = get_status.getString("text");
			String date = get_status.get("created_at").toString();
			Date dd = df.parse(date);
			Integer retweetCount = get_status.getInteger("retweet_count");
			Integer Likes = get_status.getInteger("favorite_count");
			
			Document Original_user = (Document) get_status.get("user");
			String Unique_id = Original_user.getString("id_str");
			String screen_name = Original_user.getString("screen_name");
			String name  = Original_user.getString("name");
			String lang = Original_user.getString("lang");
			Integer status_count = Original_user.getInteger("statuses_count");
			Integer friends_count = Original_user.getInteger("friends_count");
			Integer followers_count = Original_user.getInteger("followers_count");
			String description = Original_user.getString("description");
			String location = Original_user.getString("location");
			String timezone = Original_user.getString("time_zone");
			
			
			original_tweet_info.add("Status:"+text);
			original_tweet_info.add("created_at:"+dd);
			original_tweet_info.add("retweetCount:"+retweetCount);
			original_tweet_info.add("Likes:"+Likes);
			original_tweet_info.add("UniqueID_user:"+Unique_id);
			original_tweet_info.add("screen_name:"+screen_name);
			original_tweet_info.add("Name:"+name);
			original_tweet_info.add("lang:"+lang);
			original_tweet_info.add("Status_count:"+status_count);
			original_tweet_info.add("Friends_count:"+friends_count);
			original_tweet_info.add("Followers_count:"+followers_count);
			original_tweet_info.add("Description:"+description);
			original_tweet_info.add("LocationGEO:"+location);
			original_tweet_info.add("TimeZone:"+timezone);
			
		}
		original_status.append("Retweeted_status", original_tweet_info);
		
// Insert new_status into the labTweets collection
			
		   collection.insertOne(original_status);

// A counter to track your progress in the console
			//System.out.println("Inputing tweet # " + counter);
			System.out.println(original_status);
			

			counter++;
		}
		br.close();
		mongo1.close();
	}

}
