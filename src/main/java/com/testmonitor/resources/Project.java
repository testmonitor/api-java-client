package com.testmonitor.resources;

import com.testmonitor.parsers.DateParser;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private Integer id;

    private String name;

    private String description;

    private LocalDate startsAt;

    private LocalDate endsAt;

    public Project setId(Integer id) {
        this.id = id;

        return this;
    }

    public Project setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return this.id;
    }

    /**
     * Set the project name.
     *
     * @param name The name
     *
     * @return
     */
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

    public Project setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;

        return this;
    }

    public LocalDate getStartsAt() {
        return this.startsAt;
    }

    public Project setEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;

        return this;
    }

    public LocalDate getEndsAt() {
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
}
