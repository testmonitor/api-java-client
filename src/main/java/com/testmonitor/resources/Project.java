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

    /**
     * @param id
     *
     * @return
     */
    public Project setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    /**
     * @return Integer
     */
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

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param description
     *
     * @return
     */
    public Project setDescription(String description) {
        this.description = description;

        return this;
    }

    /**
     * @return
     */
    public String getDescription() {
        return name;
    }

    /**
     * @param startsAt
     *
     * @return
     */
    public Project setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;

        return this;
    }

    /**
     * @return
     */
    public LocalDate getStartsAt() {
        return this.startsAt;
    }

    /**
     * @param endsAt
     *
     * @return
     */
    public Project setEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;

        return this;
    }

    /**
     * @return
     */
    public LocalDate getEndsAt() {
        return this.endsAt;
    }

    /**
     * @return
     */
    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("starts_at", DateParser.toDateString(this.startsAt)));
        params.add(new BasicNameValuePair("ends_at", DateParser.toDateString(this.endsAt)));

        return params;
    }
}
