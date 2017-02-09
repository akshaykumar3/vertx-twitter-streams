package demo.twitter;

public class TwitterHelper {

    private static TwitterAPI twitterAPI = null;

    private static TwitterAPI api() {
        if (twitterAPI == null) {
            twitterAPI = new TwitterAPI();
        }
        return twitterAPI;
    }

    public static String get() {
        return api().getSampleStatuses();
    }
}
