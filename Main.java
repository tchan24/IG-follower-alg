import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide two JSON files as arguments.");
            return;
        }

        String followingFilePath = args[0];
        String followersFilePath = args[1];

        try {
            // Parse the following.json file
            JSONObject followingObj = (JSONObject) new JSONParser().parse(new FileReader(followingFilePath));
            Set<String> following = extractUsernames(followingObj);

            // Parse the followers_1.json file
            JSONArray followersArr = (JSONArray) new JSONParser().parse(new FileReader(followersFilePath));
            Set<String> followers = extractUsernamesFromArray(followersArr);

            // Find users that the user follows but who don't follow back
            Set<String> notFollowingBack = new HashSet<>(following);
            notFollowingBack.removeAll(followers);

            // Find users who follow the user but are not followed back
            Set<String> notFollowedByUser = new HashSet<>(followers);
            notFollowedByUser.removeAll(following);

            System.out.println("Users not following back: " + notFollowingBack);
            System.out.println("Users not followed by user: " + notFollowedByUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<String> extractUsernames(JSONObject jsonObject) {
        Set<String> usernames = new HashSet<>();
        JSONArray dataList = (JSONArray) jsonObject.get("data");
        for (Object obj : dataList) {
            JSONObject dataObj = (JSONObject) obj;
            JSONArray stringListData = (JSONArray) dataObj.get("string_list_data");
            for (Object stringData : stringListData) {
                JSONObject userData = (JSONObject) stringData;
                usernames.add((String) userData.get("value"));
            }
        }
        return usernames;
    }

    private static Set<String> extractUsernamesFromArray(JSONArray jsonArray) {
        Set<String> usernames = new HashSet<>();
        for (Object obj : jsonArray) {
            JSONObject dataObj = (JSONObject) obj;
            JSONArray stringListData = (JSONArray) dataObj.get("string_list_data");
            for (Object stringData : stringListData) {
                JSONObject userData = (JSONObject) stringData;
                usernames.add((String) userData.get("value"));
            }
        }
        return usernames;
    }
}
