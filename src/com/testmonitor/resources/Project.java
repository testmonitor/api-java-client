package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private Integer id;

    private String name;

    private String description;

    public Project() {

    }

    public Project setId(Integer id) {
        this.id = id;

        return this;
    }

    public Project setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public Project setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name;
    }

    public Project setDescription(String description) {
        this.description = description;

        return this;
    }

    public String getDescription() {
        return name;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));

        return params;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n" +
                "DESCRIPTION: " + this.description + "\n";
    }
}
