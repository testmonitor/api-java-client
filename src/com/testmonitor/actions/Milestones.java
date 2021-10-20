package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.MilestoneParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.Milestone;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Milestones
{
    private final Connector connector;

    private final String singular = "milestone";

    private final String plural = "milestones";

    private final Integer projectId;

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
        return MilestoneParser.Parse(this.connector.get(this.plural + "?page=" + page + "&project_id=" +  this.projectId));
    }

    /**
     * @return A list of milestones
     */
    public ArrayList<Milestone> list(Integer page, Integer limit)
    {
        return MilestoneParser.Parse(this.connector.get(this.plural + "?page=" + page + "&limit=" + limit + "&project_id=" +  this.projectId));
    }

    /**
     * @param id The milestone ID
     *
     * @return The milestone that matches the ID
     */
    public Milestone get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        HashMap<String, Object> milestone = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return MilestoneParser.Parse(milestone);
    }

    /**
     * Search a milestone
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<Milestone> search(String search)
    {
        return MilestoneParser.Parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Create a milestone with a given name.
     *
     * @param name The name of the milestone
     *
     * @return The created milestone
     */
    public Milestone create(String name, String endsAt)
    {
        Milestone milestone = new Milestone();

        milestone.setName(name);
        milestone.setEndsAt(endsAt);
        milestone.setProjectId(this.projectId);

        return this.create(milestone);
    }

    /**
     * Create a milestone
     *
     * @param milestone The milestone you want to create
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
     * Search or create a milestone. When the test suite is not found there will be a milestone created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test suite
     */
    public Milestone searchOrCreate(String search, String endsAt)
    {
        ArrayList<Milestone> milestones = this.search(search);

        if (milestones.size() > 0) {
            return milestones.get(0);
        }

        return this.create(search, endsAt);
    }

    /**
     * Update a milestone
     *
     * @param milestone The milestone you want to update
     *
     * @return A new instance of the milestone
     */
    public Milestone update(Milestone milestone)
    {
        JSONObject response = this.connector.put(this.plural + "/" + milestone.getId(), milestone.toHttpParams());

        HashMap<String, Object> updatedMilestone = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return MilestoneParser.Parse(updatedMilestone);
    }
}
