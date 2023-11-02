package com.testmonitor.resources;

import com.testmonitor.parsers.DateParser;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestRun {
    private Integer id;

    private String name;

    private String description;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

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

    public LocalDateTime getStartsAt() {
        return this.startsAt;
    }

    public TestRun setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;

        return this;
    }

    public LocalDateTime getEndsAt() {
        return this.endsAt;
    }

    public TestRun setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;

        return this;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000000+00:00'");
        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("milestone_id", this.milestoneId.toString()));

        if (this.startsAt != null) {
            params.add(new BasicNameValuePair("starts_at", dateTimeFormat.format(this.startsAt)));
        }

        if (this.endsAt != null) {
            params.add(new BasicNameValuePair("ends_at", dateTimeFormat.format(this.endsAt)));
        }

        return params;
    }
}
