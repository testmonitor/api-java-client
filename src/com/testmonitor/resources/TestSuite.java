package com.testmonitor.resources;

public class TestSuite {
    private Integer id;

    private String name;

    private Integer projectId;

    public TestSuite(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestSuite(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
    }

    public TestSuite() {

    }

    public Integer getId() {
        return id;
    }

    public TestSuite setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestSuite setProjectId(Integer projectId) {
        this.projectId = projectId;

        return this;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" + "NAME: " + this.name + "\n";
    }
}
