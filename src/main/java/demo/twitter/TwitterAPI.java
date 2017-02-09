package demo.twitter;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TwitterAPI {

    private String consumerKey;

    private String consumerSecret;

    private String accessToken;

    private String accessSecret;

    private Authentication auth;

    private BlockingQueue<String> queue;

    TwitterAPI() {
        this.consumerKey = getProperty("consumer_key");
        this.consumerSecret = getProperty("consumer_secret");
        this.accessToken = getProperty("access_token");
        this.accessSecret = getProperty("access_secret");
        authenticate();
        setUp();
    }

    private void authenticate() {
        auth = new OAuth1(this.consumerKey, this.consumerSecret, this.accessToken, this.accessSecret);
    }

    private void setUp() {
        queue = new LinkedBlockingDeque<>(1);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.stallWarnings(false);
        endpoint.trackTerms(Arrays.asList("US Elections", "Trump", "Bernie", "Hillary"));
        BasicClient client = new ClientBuilder().name("TestClient").hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth).processor(new StringDelimitedProcessor(queue)).build();
        client.connect();
    }

    public String getSampleStatuses() {
        try {
            JSONObject jsonObject = new JSONObject(queue.take());
            return jsonObject.get("text").toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getProperty(String name) {
        System.out.println(System.getProperty(name));
        return System.getProperty(name);
    }
}
