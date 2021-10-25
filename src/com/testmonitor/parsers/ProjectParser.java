package com.testmonitor.parsers;

import com.testmonitor.resources.Project;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectParser {
    /**
     * Parse a JSONObject in a list of projects
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of projects
     */
    public static ArrayList<Project> parse(JSONObject response)
    {
        ArrayList<Project> projects = new ArrayList<Project>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> project = (HashMap<String, Object>) obj;

            projects.add(parse(project));
        }

        return projects;
    }

    /**
     * Parse a hashmap into a project.
     *
     * @param item the hashmap that contains the project data.
     *
     * @return The parsed project
     */
    public static Project parse(HashMap<String, Object> item)
    {
        Project project = new Project();

        project.setId(item.get("id").toString())
                .setName(item.get("name").toString());

        if (item.get("description") != null) {
            project.setDescription(item.get("description").toString());
        }

        return project;
    }
}
