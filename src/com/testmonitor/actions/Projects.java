package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.resources.Project;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Projects
{
    private final Connector connector;

    private final String singular = "project";

    private final String plural = "projects";

    public Projects(Connector connector)
    {
        this.connector = connector;
    }

    public ArrayList<Project> list()
    {
        ArrayList<Project> projects = new ArrayList<Project>();

        JSONObject response = this.connector.get(this.plural);

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            projects.add(new Project(item.get("id").toString(), item.get("name").toString()));
        }

        return projects;
    }

    public Project get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new Project(
                response.getJSONObject("data").get("id").toString(),
                response.getJSONObject("data").get("name").toString()
        );
    }
}
