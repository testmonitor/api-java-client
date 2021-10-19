package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.Milestone;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Milestones
{
    private final Connector connector;

    private final String singular = "milestone";

    private final String plural = "milestones";

    private Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public Milestones(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param projectId The project id you want to work on
     */
    public Milestones(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param project The project you want to work on
     */
    public Milestones(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * Parse a JSONObject in a list of test suites
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test suites
     */
    protected ArrayList<Milestone> parse(JSONObject response)
    {
        ArrayList<Milestone> milestones = new ArrayList<Milestone>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            milestones.add(new Milestone(item.get("id").toString(), item.get("name").toString()));
        }

        return milestones;
    }

    /**
     * List all test suites
     *
     * @return A list of test suites
     */
    public ArrayList<Milestone> list()
    {
        return this.parse(this.connector.get(this.plural));
    }

    /**
     * @param id The test suite ID
     *
     * @return The test suite that matches the ID
     */
    public Milestone get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new Milestone(
            response.getJSONObject("data").get("id").toString(),
            response.getJSONObject("data").get("name").toString()
        );
    }

    /**
     * Search a test suite
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<Milestone> search(String search)
    {
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Create a test suite with a given name.
     *
     * @param name The name of the test suite
     *
     * @return The created test suite
     */
    public Milestone create(String name)
    {
        Milestone milestone = new Milestone();

        milestone.setName(name);
        milestone.setProjectId(this.projectId);

        return this.create(milestone);
    }

    /**
     * Create a test suite with a given name.
     *
     * @param name The name of the test suite
     *
     * @return The created test suite
     */
    public Milestone create(Milestone milestone)
    {
        JSONObject response = this.connector.post(this.plural, milestone.toHttpParams());

        milestone.setId(response.getJSONObject("data").get("id").toString());

        return milestone;
    }

    /**
     * Search or create a test suite. When the test suite is not found there will be a test suite created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test suite
     */
    public Milestone searchOrCreate(String search)
    {
        ArrayList<Milestone> milestones = this.search(search);

        if (milestones.size() > 0) {
            return milestones.get(0);
        }

        return this.create(search);
    }
}
