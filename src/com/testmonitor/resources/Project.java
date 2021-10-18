package com.testmonitor.resources;

import com.testmonitor.parsers.DateParser;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private Integer id;

    private String name;

    private String description;

    private Date startsAt;

    private Date endsAt;

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

    public Project setStartsAt(Date startsAt) {
        this.startsAt = startsAt;

        return this;
    }

    public Date getStartsAt() {
        return this.startsAt;
    }

    public Project setEndsAt(Date endsAt) {
        this.endsAt = endsAt;

        return this;
    }

    public Date getEndsAt() {
        return this.endsAt;
    }


    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("starts_at", DateParser.toDateString(this.startsAt)));
        params.add(new BasicNameValuePair("ends_at", DateParser.toDateString(this.endsAt)));

        return params;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n" +
                "DESCRIPTION: " + this.description + "\n" +
                "STARTS_AT: " + DateParser.toDateString(this.startsAt) + "\n" +
                "ENDS_AT: " + DateParser.toDateString(this.endsAt) + "\n";
    }
}
