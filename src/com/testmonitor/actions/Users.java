package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.UserParser;
import com.testmonitor.resources.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Users
{
    private final Connector connector;

    private final String singular = "user";

    private final String plural = "users";


    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public Users(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @return A list of users
     */
    public ArrayList<User> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of users
     */
    public ArrayList<User> list(Integer page)
    {
        return UserParser.parse(this.connector.get(this.plural + "?page=" + page));
    }

    /**
     * @return A list of users
     */
    public ArrayList<User> list(Integer page, Integer limit)
    {
        return UserParser.parse(this.connector.get(this.plural + "?page=" + page + "&limit=" + limit));
    }

    /**
     * Returns the current user that is logged in on TestMonitor.
     *
     * @return User account
     */
    public User currentUser()
    {
        JSONObject response = this.connector.get("my-account");

        HashMap<String, Object> user = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return UserParser.parse(user);
    }
}
