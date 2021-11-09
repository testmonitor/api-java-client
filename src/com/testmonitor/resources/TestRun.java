package com.testmonitor.resources;

import com.testmonitor.parsers.DateParser;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestRun {
    private Integer id;

    private String name;

    private String description;

    private Date startsAt;

    private Date endsAt;

    private Integer milestoneId;

    public TestRun setId(Integer id) {
        this.id = id;

        return this;
    }

    public TestRun setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public TestRun setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestRun setMilestoneId(Integer milestoneId) {
        this.milestoneId = milestoneId;

        return this;
    }

    public TestRun setMilestoneId(String milestoneId) {
        this.milestoneId = Integer.parseInt(milestoneId);

        return this;
    }

    public Integer getMilestoneId() {
        return this.milestoneId;
    }

    public String getDescription() {
        return this.description;
    }

    public TestRun setDescription(String description) {
        this.description = description;

        return this;
    }

    public Date getStartsAt() {
        return startsAt;
    }

    public TestRun setStartsAt(Date startsAt) {
        this.startsAt = startsAt;

        return this;
    }

    public Date getEndsAt() {
        return endsAt;
    }

    public TestRun setEndsAt(Date endsAt) {
        this.endsAt = endsAt;

        return this;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("starts_at", DateParser.toDateString(this.startsAt)));
        params.add(new BasicNameValuePair("ends_at", DateParser.toDateString(this.endsAt)));
        params.add(new BasicNameValuePair("milestone_id", this.milestoneId.toString()));

        return params;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n" +
                "DESCRIPTION: " + this.description + "\n" +
                "MILESTONE_ID: " + this.milestoneId + "\n" +
                "STARTS_AT: " + DateParser.toDateString(this.startsAt) + "\n" +
                "ENDS_AT: " + DateParser.toDateString(this.endsAt) + "\n";
    }
}
