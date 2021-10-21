package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.MilestoneParser;
import com.testmonitor.parsers.ProjectParser;
import com.testmonitor.parsers.UserParser;
import com.testmonitor.resources.Milestone;
import com.testmonitor.resources.Project;
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
     * @return A list of milestones
     */
    public ArrayList<Milestone> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of milestones
     */
    public ArrayList<Milestone> list(Integer page)
    {
        return MilestoneParser.Parse(this.connector.get(this.plural + "?page=" + page));
    }

    /**
     * @return A list of milestones
     */
    public ArrayList<Milestone> list(Integer page, Integer limit)
    {
        return MilestoneParser.Parse(this.connector.get(this.plural + "?page=" + page + "&limit=" + limit));
    }

    public User myAccount()
    {
        JSONObject response = this.connector.get("my-account");

        HashMap<String, Object> user = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return UserParser.Parse(user);
    }
}
