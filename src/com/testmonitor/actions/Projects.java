package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.ProjectParser;
import com.testmonitor.resources.Project;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Projects
{
    private final Connector connector;

    /**
     *
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public Projects(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @return A list of projects
     */
    public ArrayList<Project> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of projects
     */
    public ArrayList<Project> list(Integer page)
    {
        return ProjectParser.parse(this.connector.get("projects/?page=" + page));
    }

    /**
     * @return A list of projects
     */
    public ArrayList<Project> list(Integer page, Integer limit)
    {
        return ProjectParser.parse(this.connector.get("projects?page=" + page + "&limit=" + limit));
    }

    /**
     * Get a single project
     *
     * @param id The ID of the project
     *
     * @return The project
     */
    public Project get(Integer id)
    {
        JSONObject response = this.connector.get("projects/" + id);

        HashMap<String, Object> project = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return ProjectParser.parse(project);
    }

    /**
     * Update a project
     *
     * @param project The project you want to update
     *
     * @return A new instance of the project
     */
    public Project update(Project project)
    {
        JSONObject response = this.connector.put( "projects/" + project.getId(), project.toHttpParams());

        HashMap<String, Object> updatedProject = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return ProjectParser.parse(updatedProject);
    }
}
