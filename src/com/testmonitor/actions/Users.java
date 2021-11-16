package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.UserParser;
import com.testmonitor.resources.User;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Users
{
    private final Connector connector;

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
        return this.list(page, 15);
    }

    /**
     * @return A list of users
     */
    public ArrayList<User> list(Integer page, Integer limit)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return UserParser.parse(this.connector.get("users", params));
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
