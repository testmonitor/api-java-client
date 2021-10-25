# TestMonitor API Java Client
This is the official TestMonitor Java connector.

## Construct client
You can create a client by setting up your TestMonitor domain and API token:

```Client client = new Client("example.testmonitor.com", "t0ps3cr3t") ```

## Perform actions on the API
Most of the actions on our API are project specific. Therefore in those cases you are forced to enter a project ID.

The actions are more or less simular for every object / endpoint. I will show you some examples.

### Projects
To get a list of projects with the default pagerlimit of TestMonitor your can doe the following:

```ArrayList<Project> projects = client.projects().list();```

When you need more advance paginator options, you can use the page and limit attributes. In example the second page with a pagelimit of 15:

```ArrayList<Project> projects = client.projects().list(2,15);```

Get a single project by entering the project ID:

```Project project = client.projects().get(1);```

Update a project:
```
Project project = client.projects().get(1);
project.setName("My New Name");
client.projects().update(project);
```

### Milestones
Milestones are project specific. Therefore you MUST enter a project ID when using its endpoints.

To get a list of milestones you can do the statement below. Please note that pagination works on the same manner as on projects.

```ArrayList<Milestone> milestones = client.milestones(1).list();```

Sometimes you want to search of create a milestone when it does nog exists. The following example will search for a milestone and, if there is nothing found, it will create one with the given date. Please note that when there is a match on the name of the milestone, the date will not be updated.
```Milestone milestone = client.milestones(1).findOrCreate("My Stone", "2021-01-01")```