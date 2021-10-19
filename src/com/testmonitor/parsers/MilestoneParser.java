package com.testmonitor.parsers;

import com.testmonitor.resources.Milestone;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MilestoneParser {
    /**
     * Parse a JSONObject in a list of milestones
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of projects
     */
    public static ArrayList<Milestone> Parse(JSONObject response)
    {
        ArrayList<Milestone> milestones = new ArrayList<Milestone>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> milestone = (HashMap<String, Object>) obj;

            milestones.add(Parse(milestone));
        }

        return milestones;
    }

    /**
     * Parse a hashmap into a milestone.
     *
     * @param item the hashmap that contains the milestone data.
     *
     * @return The parsed milestone
     */
    public static Milestone Parse(HashMap<String, Object> item)
    {
        Milestone milestone = new Milestone();

        milestone.setId(item.get("id").toString())
                .setName(item.get("name").toString())
                .setProjectId(item.get("project_id").toString());

        if (item.get("description") != null) {
            milestone.setDescription(item.get("description").toString());
        }

        return milestone;
    }
}
