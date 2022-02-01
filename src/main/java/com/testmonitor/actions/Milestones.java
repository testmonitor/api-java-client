package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.MilestoneParser;
import com.testmonitor.parsers.TestCaseParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.Milestone;
import com.testmonitor.resources.TestCase;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Milestones
{
    private final Connector connector;

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector
     * @param project The TestMonitor project
     */
    public Milestones(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of milestones
     */
    public ArrayList<Milestone> list() throws IOException, URISyntaxException {
        return this.list(1);
    }

    /**
     * @param page Page number
     *
     * @return A list of milestones
     */
    public ArrayList<Milestone> list(Integer page) throws IOException, URISyntaxException {
        return this.list(page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of milestones
     */
    public ArrayList<Milestone> list(Integer page, Integer limit) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return MilestoneParser.parse(this.connector.get("milestones", params));
    }

    /**
     * @param id The milestone ID
     *
     * @return The milestone matching the ID
     */
    public Milestone get(Integer id) throws IOException {
        JSONObject response = this.connector.get("milestones/" + id);

        HashMap<String, Object> milestone = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return MilestoneParser.parse(milestone);
    }

    /**
     * Search through milestones.
     *
     * @param query The search query
     *
     * @return A list of search results
     */
    public ArrayList<Milestone> search(String query) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("query", query));

        return MilestoneParser.parse(this.connector.get("milestones", params));
    }

    /**
     * Create a milestone using the provided name. The end date will be set for next month.
     *
     * @param name The name of the milestone
     *
     * @return The created milestone
     */
    public Milestone create(String name) throws IOException {
        Milestone milestone = new Milestone();

        milestone.setName(name);
        milestone.setEndsAt(LocalDate.now().plusMonths(1));
        milestone.setProjectId(this.projectId);

        return this.create(milestone);
    }

    /**
     * Create a milestone.
     *
     * @param milestone The new milestone
     *
     * @return The created milestone
     */
    public Milestone create(Milestone milestone) throws IOException {
        JSONObject response = this.connector.post("milestones", milestone.toHttpParams());

        milestone.setId(response.getJSONObject("data").get("id").toString());

        return milestone;
    }

    /**
     * Find a milestone using the provided query or create a new one.
     *
     * @param name The search query
     *
     * @return A milestone matching the query or a new milestone.
     */
    public Milestone findOrCreate(String name) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("filter[name]", name));

        ArrayList<Milestone> milestones = MilestoneParser.parse(this.connector.get("milestones", params));

        if (milestones.size() > 0) {
            return milestones.get(0);
        }

        return this.create(name);
    }

    /**
     * Update a milestone.
     *
     * @param milestone The milestone you want to update
     *
     * @return The updated milestone
     */
    public Milestone update(Milestone milestone) throws IOException {
        JSONObject response = this.connector.put("milestones/" + milestone.getId(), milestone.toHttpParams());

        HashMap<String, Object> updatedMilestone = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return MilestoneParser.parse(updatedMilestone);
    }
}
