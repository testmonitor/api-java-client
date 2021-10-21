package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer id;

    private String name;

    public User setId(Integer id) {
        this.id = id;

        return this;
    }

    public User setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public User setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("name", this.name));

        return params;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n";
    }
}
