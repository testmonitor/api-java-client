package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.ProjectParser;
import com.testmonitor.resources.Project;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class Projects
{
    private final Connector connector;

    /**
     * @param connector The TestMonitor connector
     */
    public Projects(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @return A list of projects
     */
    public ArrayList<Project> list() throws IOException, URISyntaxException {
        return this.list(1);
    }

    /**
     * @param page Page number
     *
     * @return A list of projects
     */
    public ArrayList<Project> list(Integer page) throws IOException, URISyntaxException {
        return this.list(page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of projects
     */
    public ArrayList<Project> list(Integer page, Integer limit) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return ProjectParser.parse(this.connector.get("projects", params));
    }

    /**
     * Get a single project
     *
     * @param id The ID of the project
     *
     * @return The project
     */
    public Project get(Integer id) throws IOException {
        JSONObject response = this.connector.get("projects/" + id);

        HashMap<String, Object> project = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return ProjectParser.parse(project);
    }

    /**
     * Update a project
     *
     * @param project The project you want to update
     *
     * @return The updated project
     */
    public Project update(Project project) throws IOException {
        JSONObject response = this.connector.put( "projects/" + project.getId(), project.toHttpParams());

        HashMap<String, Object> updatedProject = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return ProjectParser.parse(updatedProject);
    }
}
