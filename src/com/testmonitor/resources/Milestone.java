package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Milestone {
    private Integer id;

    private String name;

    private String description;

    private Integer projectId;

    public Milestone() {

    }

    public Milestone setId(Integer id) {
        this.id = id;

        return this;
    }

    public Milestone setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public Milestone setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public Milestone setDescription(String description) {
        this.description = description;

        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Milestone setProjectId(Integer projectId) {
        this.projectId = projectId;

        return this;
    }

    public Milestone setProjectId(String projectId) {
        this.projectId = Integer.parseInt(projectId);

        return this;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return params;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n" +
                "DESCRIPTION: " + this.description + "\n" +
                "PROJECT_ID: " + this.projectId + "\n";
    }
}
