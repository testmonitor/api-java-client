package com.testmonitor.resources;

public class Project {
    private final Integer id;

    private final String name;

    public Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" + "NAME: " + this.name + "\n";
    }
}
