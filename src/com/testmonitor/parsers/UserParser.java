package com.testmonitor.parsers;

import com.testmonitor.resources.Project;
import com.testmonitor.resources.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserParser {
    /**
     * Parse a JSONObject in a list of users
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of users
     */
    public static ArrayList<User> Parse(JSONObject response)
    {
        ArrayList<User> users = new ArrayList<User>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> user = (HashMap<String, Object>) obj;

            users.add(Parse(user));
        }

        return users;
    }

    /**
     * Parse a hashmap into a user.
     *
     * @param item the hashmap that contains the user data.
     *
     * @return The parsed project
     */
    public static User Parse(HashMap<String, Object> item)
    {
        User user = new User();

        user.setId(item.get("id").toString())
                .setName(item.get("name").toString());

        return user;
    }
}
