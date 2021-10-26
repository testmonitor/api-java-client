package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
    private Integer id;

    private String name;

    private String description;

    private Integer testSuiteId;

    public TestCase setId(Integer id) {
        this.id = id;

        return this;
    }

    public TestCase setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public TestCase setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestCase setTestSuiteId(Integer testSuiteId) {
        this.testSuiteId = testSuiteId;

        return this;
    }

    public TestCase setTestSuiteId(String testSuiteId) {
        this.testSuiteId = Integer.parseInt(testSuiteId);

        return this;
    }

    public Integer getTestSuiteId() {
        return this.testSuiteId;
    }

    public String getDescription() {
        return this.description;
    }

    public TestCase setDescription(String description) {
        this.description = description;

        return this;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("test_suite_id", this.testSuiteId.toString()));

        return params;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" +
                "NAME: " + this.name + "\n" +
                "TEST_SUITE_ID: " + this.testSuiteId + "\n";
    }
}
